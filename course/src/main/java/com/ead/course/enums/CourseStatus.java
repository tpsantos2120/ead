package com.ead.course.enums;

public enum CourseStatus {

    IN_PROGRESS("IN_PROGRESS"),
    CONCLUDED("CONCLUDED");

    private final String courseStatus;

    CourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getCourseStatus() {
        return courseStatus;
    }
}
