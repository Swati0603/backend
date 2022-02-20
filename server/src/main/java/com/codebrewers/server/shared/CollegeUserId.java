package com.codebrewers.server.shared;

import com.codebrewers.server.models.College;

public class CollegeUserId {

    private College college;
    private Long userId;

    public CollegeUserId(College college, Long userId) {
        this.college = college;
        this.userId = userId;
    }

    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
