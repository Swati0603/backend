package com.codebrewers.server.shared;

import com.codebrewers.server.models.Company;

public class CompanyUserId {

    private Company company;
    private long userId;

    public CompanyUserId(Company company, long userId) {
        this.company = company;
        this.userId = userId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}

