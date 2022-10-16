package com.ead.course.clients;

import com.ead.course.dtos.ResponsePageDTO;
import com.ead.course.dtos.UserDTO;
import com.ead.course.services.UtilsService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
@Log4j2
public class AuthUserClient {

    private final WebClient webClient;
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
        log.info("Ending request /courses courseId {} ", courseId);
        return result.getBody();
    }

    public ResponseEntity<UserDTO> getOneUserById(UUID userId) {
        String url = utilsService.createUrlForGetOneUserById(userId);
        log.debug("Request URL: {} ", url);
        log.info("Request URL: {} ", url);
        return webClient.method(HttpMethod.GET)
                .uri(url)
                .retrieve().onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    log.error("Error request /users/{}", userId);
                    return Mono.error(new WebClientResponseException(
                            "Error request /users/" + userId,
                            clientResponse.rawStatusCode(),
                            clientResponse.statusCode().getReasonPhrase(),
                            clientResponse.headers().asHttpHeaders(),
                            null,
                            null));
                })
                .toEntity(UserDTO.class)
                .block();

    }
}
