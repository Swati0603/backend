package com.codebrewers.server.payload.dto;

import com.codebrewers.server.shared.UserName;
import com.codebrewers.server.shared.UserType;

public class UserRegistrationDto {
    private UserName userName;
    private String mobileNumber;
    private String email;
    private String password;
    private UserType userType;

    public UserRegistrationDto() {
        this.userType = UserType.USER;
    }

    public UserRegistrationDto(UserName userName, String mobileNumber, String email, String password) {
        this.userName = userName;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.password = password;

        // By default
        this.userType = UserType.USER;
    }

    public UserName getUserName() {
        return userName;
    }

    public void setUserName(UserName userName) {
        this.userName = userName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
