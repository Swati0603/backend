package com.codebrewers.server.services;

import com.codebrewers.server.models.SkillSet;
import com.codebrewers.server.repos.SkillSetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillSetService {
    
    @Autowired
    SkillSetRepo skillSetRepo;

    public SkillSet registerSkillSet(SkillSet skillSet) {
        return skillSetRepo.save(skillSet);
    }

    public List<SkillSet> getAllSkillSets() {
        return skillSetRepo.findAll();
    }
}
