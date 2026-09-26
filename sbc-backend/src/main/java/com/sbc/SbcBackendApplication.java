package com.sbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SbcBackendApplication {

    private static final Logger log = LoggerFactory.getLogger(SbcBackendApplication.class);

    public static void main(String[] args) {
        log.info("Starting Sahyadri BConnect backend application");
        SpringApplication.run(SbcBackendApplication.class, args);
    }

}
