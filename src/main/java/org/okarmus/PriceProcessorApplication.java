package org.okarmus;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class PriceProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(PriceProcessorApplication.class, args);
	}
}
