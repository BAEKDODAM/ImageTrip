package com.ImageTrip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ImageTripApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageTripApplication.class, args);
	}

}
