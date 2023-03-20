package com.ead.authuser.enums;

public enum CourseLevel {

    BEGINNER("BEGINNER"),
    INTERMEDIARY("INTERMEDIARY"),
    ADVANCED("ADVANCED");

    private final String courseLevel;

    CourseLevel(String courseLevel) {
        this.courseLevel = courseLevel;
    }

    public String getCourseLevel() {
        return courseLevel;
    }
}
