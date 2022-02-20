package com.codebrewers.server.controllers;

import java.util.ArrayList;
import java.util.List;

import com.codebrewers.server.models.SkillSet;
import com.codebrewers.server.services.SkillSetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path="/api/skillsets")
public class SkillSetController {

    @Autowired
    SkillSetService skillSetService;

    @GetMapping()
    public ResponseEntity<List<SkillSet>> displaySkillSets() {
        try {
            List<SkillSet> allSkillSets = new ArrayList<SkillSet>();
            allSkillSets = skillSetService.getAllSkillSets();
            if (allSkillSets.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(allSkillSets, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
