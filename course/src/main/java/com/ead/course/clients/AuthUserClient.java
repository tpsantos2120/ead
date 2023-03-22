package com.ead.course.clients;

import com.ead.course.dtos.CourseUserDTO;
import com.ead.course.dtos.ResponsePageDTO;
import com.ead.course.dtos.UserDTO;
import com.ead.course.services.UtilsService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
@Log4j2
public class AuthUserClient {

    private final RestTemplate restTemplate;
    private final UtilsService utilsService;

    public Page<UserDTO> getAllCoursesByUserId(Pageable pageable, UUID courseId) {
        List<UserDTO> searchResult = null;
        ResponseEntity<ResponsePageDTO<UserDTO>> result = null;
        String url = utilsService.createUrlForGetAllUsersByCourseId(courseId, pageable);
        log.debug("Request URL: {} ", url);
        log.info("Request URL: {} ", url);
        try {
            ParameterizedTypeReference<ResponsePageDTO<UserDTO>> responseType = new ParameterizedTypeReference<>() {
            };
            result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            searchResult = result.getBody().getContent();
            log.debug("Response Number of Elements: {} ", searchResult.size());
        } catch (HttpStatusCodeException e) {
            log.error("Error request /courses", e);
        }
        log.info("Ending request /courses courseId {} ", courseId);
        return result.getBody();
    }

    public ResponseEntity<UserDTO> getOneUserById(UUID userId) {
        String url = utilsService.createUrlForGetOneUserById(userId);
        log.debug("Request URL: {} ", url);
        log.info("Request URL: {} ", url);
        return restTemplate.exchange(url, HttpMethod.GET, null, UserDTO.class);
    }

    public void postSubscriptionUserToCourse(UUID courseId, UUID userId) {
        var courseUserDTO = new CourseUserDTO();
        courseUserDTO.setCourseId(courseId);
        courseUserDTO.setUserId(userId);
        String url = utilsService.createUrlForSubscriptionUserToCourse(userId);
        log.debug("Request URL: {} ", url);
        log.info("Request URL: {} ", url);
        restTemplate.postForEntity(url, courseUserDTO, Void.class);
    }

    public void deleteCourseInAuthUser(UUID courseId) {
        String url = utilsService.createUrlForUserCourseDeletion(courseId);
        log.debug("Request URL: {} ", url);
        log.info("Request URL: {} ", url);
        restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
    }
}
