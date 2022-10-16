package com.ead.authuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(
        basePackages = "com.ead.clients"
)
public class AuthUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthUserApplication.class, args);
    }

}
