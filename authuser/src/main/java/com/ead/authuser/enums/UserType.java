package com.ead.authuser.enums;

public enum UserType {

    ADMIN("ADMIN"),
    STUDENT("STUDENT"),
    INSTRUCTOR("INSTRUCTOR");

    private final String userStatus;

    UserType(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserType() {
        return userStatus;
    }

}
