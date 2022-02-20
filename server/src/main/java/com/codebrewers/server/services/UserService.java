package com.codebrewers.server.services;

import java.util.*;

import com.codebrewers.server.exception.ResourceConflictException;
import com.codebrewers.server.exception.ResourceNotFoundException;
import com.codebrewers.server.models.College;
import com.codebrewers.server.models.Company;
import com.codebrewers.server.models.User;
import com.codebrewers.server.payload.dto.UserRegistrationDto;
import com.codebrewers.server.repos.UserRepo;

import com.codebrewers.server.shared.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.core.userdetails.UserDetailsService;

@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    UserRepo userRepo;

    @Autowired
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public User save(UserRegistrationDto userRegistrationDto) throws ResourceConflictException{
        if(userRepo.existsByEmail(userRegistrationDto.getEmail())){
            throw new ResourceConflictException("Email is already taken");
        }

        User user = new User(userRegistrationDto.getUserName(),
                userRegistrationDto.getMobileNumber(),
                userRegistrationDto.getEmail(),
                passwordEncoder().encode(userRegistrationDto.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByEmail(username);

        if (user.isPresent()) {
            User currentUser = user.get();

            List<SimpleGrantedAuthority> role = new ArrayList<>();
            role.add(new SimpleGrantedAuthority(currentUser.getUserType().toString()));

            return new org.springframework.security.core.userdetails.User(currentUser.getEmail(), currentUser.getPassword(), role);
        }
        else {
            throw new UsernameNotFoundException("Could not find user");
        }
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepo.findById(id);
    }

    public Optional<User> getUserByEmail(String userEmail) {
        return userRepo.findByEmail(userEmail);
    }

    public Optional<User> getUserByUserName(String userName) {
        return userRepo.findByUserName(userName);
    }

    public List<User> getUsersByCompany(Company company) {
        return userRepo.findByUserCompany(company);
    }

    public List<User> getUsersByCollege(College college) {
        return userRepo.findByUserCollege(college);
    }

    public User updateUserApprovalStatus(Long userId) throws ResourceNotFoundException{
        Optional<User> user = this.getUserById(userId);
        if(user.isPresent()) {
            User currentUser = user.get();
            currentUser.setAdminApprovalStatus(true);
            return userRepo.save(currentUser);
        }
        else {
            throw new ResourceNotFoundException("User not found!");
        }
    }

    public User updateUserCompany(Long userId, Company company) throws ResourceNotFoundException{
        Optional<User> user = this.getUserById(userId);
        if(user.isPresent()) {
            User currentUser = user.get();
            currentUser.setUserCompany(company);
            return userRepo.save(currentUser);
        }
        else {
            throw new ResourceNotFoundException("User not found!");
        }
    }

    public User updateUserCollege(Long userId, College college) throws ResourceNotFoundException{
        Optional<User> user = this.getUserById(userId);
        if(user.isPresent()) {
            User currentUser = user.get();
            currentUser.setUserCollege(college);
            return userRepo.save(currentUser);
        }
        else {
            throw new ResourceNotFoundException("User not found!");
        }
    }
}




