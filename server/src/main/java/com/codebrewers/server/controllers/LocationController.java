package com.codebrewers.server.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.codebrewers.server.models.Location;
import com.codebrewers.server.services.LocationService;

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
@RequestMapping(path="/api/locations")
public class LocationController {

    @Autowired
    LocationService locationService;

    @GetMapping()
    public ResponseEntity<List<Location>> displayLocations(
            @RequestParam(required = false) Map<String, String> allParams) {
        try {
            List<Location> allLocations = new ArrayList<Location>();

            if (allParams.isEmpty()) {
                allLocations = locationService.getLocations();
            } else {
                allLocations = locationService.getAllLocationsCustom(allParams);
            }

            if (allLocations.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(allLocations, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
