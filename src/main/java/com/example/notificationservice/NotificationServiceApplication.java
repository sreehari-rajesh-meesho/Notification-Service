package com.example.notificationservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotificationServiceApplication {

    private static final Logger LOG = LogManager.getLogger(NotificationServiceApplication.class);

    public static void main(String[] args) {
        LOG.info("Application started");
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}
