package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseDTO;
import com.ead.authuser.dtos.ResponsePageDTO;
import com.ead.authuser.service.UtilsService;
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
@Log4j2
@AllArgsConstructor
public class CourseClient {

    private final RestTemplate restTemplate;
    private final UtilsService utilsService;

    public Page<CourseDTO> getAllCoursesByUserId(Pageable pageable, UUID userId) {
        List<CourseDTO> searchResult = null;
        ResponseEntity<ResponsePageDTO<CourseDTO>> result = null;
        String url = utilsService.createUrlForGetAllCoursesByUserId(userId, pageable);
        log.debug("Request URL: {} ", url);
        log.info("Request URL: {} ", url);
        try {
            ParameterizedTypeReference<ResponsePageDTO<CourseDTO>> responseType = new ParameterizedTypeReference<>() {
            };
            result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            searchResult = result.getBody().getContent();
            log.debug("Response Number of Elements: {} ", searchResult.size());
        } catch (HttpStatusCodeException e) {
            log.error("Error request /courses", e);
        }
        log.info("Ending request /courses userId {} ", userId);
        return result.getBody();
    }
}
