package com.codebrewers.server.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="skill_set")
public class SkillSet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="skill_set_id")
    private Long skillSetId;

    @Column(name="skill_set_name", nullable = false)
    private String skillSetName;

    @JsonIgnore
    @ManyToMany(mappedBy = "skillSet")
    private List<JobPost> jobPost;

    public SkillSet() {
    }

    public SkillSet(String skillSetName, List<JobPost> jobPost) {
        this.skillSetName = skillSetName;
        this.jobPost = jobPost;
    }

    public Long getSkillSetId() {
        return skillSetId;
    }

    public void setSkillSetId(Long skillSetId) {
        this.skillSetId = skillSetId;
    }

    public String getSkillSetName() {
        return skillSetName;
    }

    public void setSkillSetName(String skillSetName) {
        this.skillSetName = skillSetName;
    }

    @JsonIgnore
    public List<JobPost> getJobPost() {
        return jobPost;
    }

    @JsonIgnore
    public void setJobPost(List<JobPost> jobPost) {
        this.jobPost = jobPost;
    }
}
