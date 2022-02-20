package com.codebrewers.server.controllers;

import com.codebrewers.server.exception.ResourceConflictException;
import com.codebrewers.server.models.User;
import com.codebrewers.server.payload.dto.UserLoginDto;
import com.codebrewers.server.payload.dto.UserRegistrationDto;
import com.codebrewers.server.repos.UserRepo;
import com.codebrewers.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path="/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        try {
            userService.save(userRegistrationDto);
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        }
        catch (ResourceConflictException e) {
            return new ResponseEntity<>("Email is already taken", HttpStatus.CONFLICT);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginDto userLoginDto){
        try {
            Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new ResponseEntity<>("User signed-in successfully",HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            return new ResponseEntity<>("FML",HttpStatus.UNAUTHORIZED);
        }
    }
}
