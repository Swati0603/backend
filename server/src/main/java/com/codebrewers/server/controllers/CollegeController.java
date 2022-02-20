package com.codebrewers.server.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.codebrewers.server.exception.ResourceNotFoundException;
import com.codebrewers.server.models.College;
import com.codebrewers.server.services.CollegeService;

import com.codebrewers.server.services.UserService;
import com.codebrewers.server.shared.CollegeUserId;
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
@RequestMapping(path="/api/colleges")
public class CollegeController {

    @Autowired
    CollegeService collegeService;

    @Autowired
    UserService userService;

    @GetMapping()
    public ResponseEntity<List<College>> displayColleges(
            @RequestParam(required = false) Map<String, String> allParams) {
        try {
            List<College> allColleges = new ArrayList<College>();

            if (allParams.isEmpty()) {
                allColleges = collegeService.getAllColleges();
            } else {
                allColleges = collegeService.getAllCollegesCustom(allParams);
            }

            if (allColleges.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(allColleges, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<College> displayCollegeById(@PathVariable("id") long id) {
        try {
            Optional<College> college = collegeService.getCollegeById(id);
            return college.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<College> createCollege(@RequestBody CollegeUserId collegeUserId) {
        try {
            ResponseEntity<College> college = new ResponseEntity<>(collegeService.registerCollege(collegeUserId.getCollege()), HttpStatus.CREATED);

            long userId = collegeUserId.getUserId();
            userService.updateUserApprovalStatus(userId);
            userService.updateUserCollege(userId, collegeUserId.getCollege());

            return college;

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCollege(@PathVariable long id) {
        try {
            collegeService.deleteCollege(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<College> updateCollege(@PathVariable("id") long id, @RequestBody College college) {
        try {
            return new ResponseEntity<>(collegeService.updateCollege(college), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
