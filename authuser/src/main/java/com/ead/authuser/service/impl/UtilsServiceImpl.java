package com.ead.authuser.service.impl;

import com.ead.authuser.service.UtilsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilsServiceImpl implements UtilsService {

    @Value("${ead.api.url.course}")
    private String REQUEST_URI;

    public String createUrlForGetAllCoursesByUserId(UUID userId, Pageable pageable) {
        return REQUEST_URI +
                "?userId=" + userId +
                "&page=" + pageable.getPageNumber() +
                "&size=" + pageable.getPageSize() +
                "&sort=" + pageable.getSort().toString().replaceAll(":\\s+", ",");
    }

    @Override
    public String createUrlForCourseUserDeletion(UUID userId) {
        return REQUEST_URI + "/users/" + userId;
    }
}

