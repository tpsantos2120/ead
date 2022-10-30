package com.ead.course.services.impl;

import com.ead.course.services.UtilsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilsServiceImpl implements UtilsService {

    @Value("${ead.api.url.authuser}")
    private String REQUEST_URI;

    @Override
    public String createUrlForGetAllUsersByCourseId(UUID courseId, Pageable pageable) {
        return REQUEST_URI +
                "?courseId=" + courseId +
                "&page=" + pageable.getPageNumber() +
                "&size=" + pageable.getPageSize() +
                "&sort=" + pageable.getSort().toString().replaceAll(":\\s+", ",");
    }

    @Override
    public String createUrlForGetOneUserById(UUID userId) {
        return REQUEST_URI + "/" + userId;
    }

    @Override
    public String createUrlForSubscriptionUserToCourse(UUID userId) {
        return REQUEST_URI + "/" + userId + "/courses/subscription";
    }
}
