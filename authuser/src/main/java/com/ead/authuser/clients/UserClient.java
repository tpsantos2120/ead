package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseDTO;
import com.ead.authuser.dtos.ResponsePageDto;
import com.ead.authuser.service.UtilsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@Log4j2
public class UserClient {

    private final WebClient webClient;
    private final UtilsService utilsService;

    @Autowired
    public UserClient(WebClient webClient, UtilsService utilsService) {
        this.webClient = webClient;
        this.utilsService = utilsService;
    }

    public Page<CourseDTO> getAllCoursesByUserId(Pageable pageable, UUID userId) {
        List<CourseDTO> searchResult = null;
        ResponseEntity<ResponsePageDto<CourseDTO>> result = null;
        String url = utilsService.createUrlForGetAllCoursesByUserId(userId, pageable);
        log.debug("Request URL: {} ", url);
        log.info("Request URL: {} ", url);
        try {
            ParameterizedTypeReference<ResponsePageDto<CourseDTO>> responseType = new ParameterizedTypeReference<>() {
            };
            result = webClient.method(HttpMethod.GET)
                    .uri(url)
                    .retrieve()
                    .toEntity(responseType)
                    .block();
            searchResult = result.getBody().getContent();
            log.debug("Response Number of Elements: {} ", searchResult.size());
        } catch (HttpStatusCodeException e) {
            log.error("Error request /courses", e);
        }
        log.info("Ending request /courses userId {} ", userId);
        return result.getBody();
    }
}
