package com.codebrewers.server.services;

import static java.lang.Double.parseDouble;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.codebrewers.server.exception.ResourceNotFoundException;
import com.codebrewers.server.models.JobPost;
import com.codebrewers.server.models.Location;
import com.codebrewers.server.models.Qualification;
import com.codebrewers.server.models.SkillSet;
import com.codebrewers.server.repos.JobPostRepo;
import com.codebrewers.server.shared.JobDomain;
import com.codebrewers.server.shared.JobOpenFor;
import com.codebrewers.server.shared.JobType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobPostService {
    
    @Autowired
    JobPostRepo jobPostRepo;

    public HashSet<JobPost> getJobsByQualification(List<Qualification> qualifications) {
        return jobPostRepo.findByQualificationIn(qualifications);
    }

    public HashSet<JobPost> getJobsByLocation(List<Location> locations) {
        return jobPostRepo.findByJobLocationIn(locations);
    }

    public HashSet<JobPost> getJobBySkillSet(List<SkillSet> skillSets) {
        return jobPostRepo.findBySkillSetIn(skillSets);
    }

    public List<JobPost> getJobsByOpenedFor(JobOpenFor jobOpenFor) {
        return jobPostRepo.findByJobOpenFor(jobOpenFor);
    }

    public Optional<JobPost> getJobPostById(Long id) {
        return jobPostRepo.findById(id);
    }

    public List<JobPost> getAllJobPosts() {
        return jobPostRepo.findAll();
    }

    public List<JobPost> getJobPostsByType(JobType jobType) {
        return jobPostRepo.findByJobType(jobType);
    }

    public List<JobPost> getJobPostsByDomain(JobDomain jobDomain) {
        return jobPostRepo.findByJobDomain(jobDomain);
    }

    public List<JobPost> getJobPostsByRole(String jobRole) {
        return jobPostRepo.findByJobRole(jobRole);
    }

    public List<JobPost> getJobPostsByCompensation(double compensation) {
        return jobPostRepo.findByJobCompensation(compensation);
    }

    public JobPost registerJobPost(JobPost jobPost) {
        return jobPostRepo.save(jobPost);
    }

    public JobPost updateJobPost(JobPost jobPost) throws ResourceNotFoundException {
        Optional<JobPost> current = jobPostRepo.findById(jobPost.getJobPostId());
        if (current.isPresent()) {
            JobPost currentJobPost = current.get();
            currentJobPost.setJobCompensation(jobPost.getJobCompensation());
            currentJobPost.setJobDescription(jobPost.getJobDescription());
            currentJobPost.setJobDomain(jobPost.getJobDomain());
            currentJobPost.setJobOpenFor(jobPost.getJobOpenFor());
            currentJobPost.setJobType(jobPost.getJobType());
            currentJobPost.setJobRole(jobPost.getJobRole());
            return jobPostRepo.save(currentJobPost);
        } else {
            throw new ResourceNotFoundException("JobPost Not Found");
        }
    }

    public void deleteJobPost(Long jobPostId) throws ResourceNotFoundException {
        Optional<JobPost> current = jobPostRepo.findById(jobPostId);
        if (current.isPresent()) {
            jobPostRepo.deleteById(jobPostId);
        } else {
            throw new ResourceNotFoundException("JobPost Not Found");
        }
    }

    public List<JobPost> getAllJobPostsCustom(Map<String, String> allParams) {
        // Extract params from url to pass into service

        String jobRole = allParams.get("jobrole") != null ? allParams.get("jobrole") : null;

        JobType jobType = allParams.get("jobtype") != null ? Enum.valueOf(JobType.class, allParams.get("jobtype"))
                : null;
        JobDomain jobDomain = allParams.get("jobdomain") != null
                ? Enum.valueOf(JobDomain.class, allParams.get("jobdomain"))
                : null;
        JobOpenFor jobOpenFor = allParams.get("jobopenfor") != null
                ? Enum.valueOf(JobOpenFor.class, allParams.get("jobopenfor"))
                : null;

        Double compensation_max = allParams.get("cmax") != null ? parseDouble(allParams.get("cmax")) : null;
        Double compensation_min = allParams.get("cmin") != null ? parseDouble(allParams.get("cmin")) : null;

        return jobPostRepo.findByJobPostCustom(jobRole, jobType, jobDomain, jobOpenFor, compensation_max,
                compensation_min);
    }
}
