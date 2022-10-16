package com.ead.authuser.enums;

public enum UserStatus {

    ACTIVE("ACTIVE"),
    BLOCKED("BLOCKED");

    private final String userType;

    UserStatus(String userType) {
        this.userType = userType;
    }

    public String getUserStatus() {
        return userType;
    }
}
