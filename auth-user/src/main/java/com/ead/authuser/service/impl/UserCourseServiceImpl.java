package com.ead.authuser.service.impl;

import com.ead.authuser.repositories.UserCourseRepository;
import com.ead.authuser.service.UserCourseService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserCourseServiceImpl implements UserCourseService {

    private final UserCourseRepository userCourseRepository;

    @Autowired
    public UserCourseServiceImpl(UserCourseRepository userCourseRepository) {
        this.userCourseRepository = userCourseRepository;
    }
}
