package com.ead.course.services.impl;

import com.ead.course.services.UtilsService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilsServiceImpl implements UtilsService {

    private static final String REQUEST_URI = "http://localhost:8087/v1/users";

    @Override
    public String createUrlForGetAllUsersByCourseId(UUID courseId, Pageable pageable) {
        return REQUEST_URI +
                "?courseId=" + courseId +
                "&page=" + pageable.getPageNumber() +
                "&size=" + pageable.getPageSize() +
                "&sort=" + pageable.getSort().toString().replaceAll(":\\s+", ",");
    }
}
