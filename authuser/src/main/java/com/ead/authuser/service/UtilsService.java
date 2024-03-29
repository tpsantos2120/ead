package com.ead.authuser.service;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UtilsService {

    String createUrlForGetAllCoursesByUserId(UUID userId, Pageable pageable);

    String createUrlForCourseUserDeletion(UUID userId);
}
