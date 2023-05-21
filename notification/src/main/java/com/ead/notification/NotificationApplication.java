package com.ead.notification;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@Slf4j
public class NotificationApplication {

    public static void main(String[] args) {

        SpringApplication.run(NotificationApplication.class, args);

    }

    @Bean
    CommandLineRunner run() {
        return args -> {
            Logger logger = LoggerFactory.getLogger("jsonLogger");
            logger.info("Debug message");
            log.info(List.of("FUCK", "ME").toString());
        };
    }

}
