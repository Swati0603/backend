package com.codebrewers.server.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.codebrewers.server.models.Qualification;
import com.codebrewers.server.services.QualificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path="/api/qualifications")
public class QualificationController {

    @Autowired
    QualificationService qualificationService;

    @GetMapping()
    public ResponseEntity<List<Qualification>> displayQualification(
            @RequestParam(required = false) Map<String, String> allParams) {
        try {
            List<Qualification> allQualifications = new ArrayList<Qualification>();

            if (allParams.isEmpty()) {
                allQualifications = qualificationService.getAllQualifications();
            } else {
                allQualifications = qualificationService.getAllQualificationsCustom(allParams);
            }

            if (allQualifications.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(allQualifications, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
