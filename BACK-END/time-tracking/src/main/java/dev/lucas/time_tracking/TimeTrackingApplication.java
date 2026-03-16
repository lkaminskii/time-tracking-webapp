package dev.lucas.time_tracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"dev.lucas.time_tracking.repository"})
@EntityScan(basePackages = {"dev.lucas.time_tracking.model"})
@SpringBootApplication
public class TimeTrackingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimeTrackingApplication.class, args);
	}

}
