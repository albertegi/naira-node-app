package com.alvirg.ecommerce;

import com.alvirg.ecommerce.notification.Notification;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class NotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

    //@Bean
    CommandLineRunner ensureNotificationDatabase(MongoTemplate mongoTemplate) {
        return args -> {
            try {
                mongoTemplate.createCollection(Notification.class);
            } catch (Exception ignored) {
                /* collection already exists */
            }
        };
    }
}
