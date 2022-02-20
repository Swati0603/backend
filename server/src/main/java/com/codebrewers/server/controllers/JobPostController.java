package com.codebrewers.server.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.codebrewers.server.exception.ResourceNotFoundException;
import com.codebrewers.server.models.JobPost;
import com.codebrewers.server.services.JobPostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/api/jobposts")
public class JobPostController {

    @Autowired
    JobPostService jobPostService;

    @GetMapping()
    public ResponseEntity<List<JobPost>> displayJobPosts(@RequestParam(required=false) Map<String,String> allParams) {
        try{
            List<JobPost> allJobPosts = new ArrayList<JobPost>();

            if(allParams.isEmpty()) {
                allJobPosts = jobPostService.getAllJobPosts();
            }
            else {
                allJobPosts= jobPostService.getAllJobPostsCustom(allParams);
            }

            if(allJobPosts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            else {
                return new ResponseEntity<>(allJobPosts, HttpStatus.OK);
            }
        }
        catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPost> displayJobPostById(@PathVariable("id") long id) {
        try {
            Optional<JobPost> jobPost = jobPostService.getJobPostById(id);
            return jobPost.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<JobPost> createJobPost(@RequestBody JobPost jobPost) {
        try {
            return new ResponseEntity<>(jobPostService.registerJobPost(jobPost), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteJobPost(@PathVariable long id) {
        try {
            jobPostService.deleteJobPost(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobPost> updateJobPost(@PathVariable("id") long id, @RequestBody JobPost jobPost) {
        try {
            return new ResponseEntity<>(jobPostService.updateJobPost(jobPost), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
