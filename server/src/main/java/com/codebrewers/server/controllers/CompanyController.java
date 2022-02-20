package com.codebrewers.server.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.codebrewers.server.exception.ResourceNotFoundException;
import com.codebrewers.server.models.Company;
import com.codebrewers.server.services.CompanyService;

import com.codebrewers.server.services.UserService;
import com.codebrewers.server.shared.CompanyUserId;
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
@RequestMapping(path="/api/companies")
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @Autowired
    UserService userService;

    @GetMapping()
    public ResponseEntity<List<Company>> displayCompanies(@RequestParam(required = false) String name) {
        try {
            List<Company> allCompanies = new ArrayList<Company>();

            if (name == null) {
                allCompanies = companyService.getAllCompanies();
            } else {
                allCompanies = companyService.getCompaniesByName(name);
            }

            if (allCompanies.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(allCompanies, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> displayCompanyById(@PathVariable("id") long id) {
        try {
            Optional<Company> company = companyService.getCompanyById(id);
            return company.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<Company> createCompany(@RequestBody CompanyUserId companyUserId) {
        // Admin Only
        try {
            ResponseEntity<Company> currentCompany= new ResponseEntity<>(companyService.registerCompany(companyUserId.getCompany()), HttpStatus.CREATED);

            Long userId = companyUserId.getUserId();
            userService.updateUserApprovalStatus(userId);
            userService.updateUserCompany(userId, companyUserId.getCompany());

            return currentCompany;
        } catch (Exception e) {
            System.out.print(e.getLocalizedMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCompany(@PathVariable long id) {
        try {
            companyService.deleteCompany(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable("id") long id, @RequestBody Company company) {
        try {
            return new ResponseEntity<>(companyService.updateCompany(company), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


