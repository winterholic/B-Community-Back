package com.bjt.gallery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BjtGalleryApplication {

    public static void main(String[] args) {
        SpringApplication.run(BjtGalleryApplication.class, args);
    }

}
