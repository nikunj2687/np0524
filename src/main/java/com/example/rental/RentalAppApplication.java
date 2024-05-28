package com.example.rental;

import com.example.rental.model.Tool;
import com.example.rental.repository.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RentalAppApplication implements CommandLineRunner {

	@Autowired
	private ToolRepository toolRepository;

	public static void main(String[] args) {
		SpringApplication.run(RentalAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		toolRepository.save(new Tool("CHNS", "Chainsaw", "Stihl", 1.49, true, false, true));
		toolRepository.save(new Tool("LADW", "Ladder", "Werner", 1.99, true, true, false));
		toolRepository.save(new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, true, false, false));
		toolRepository.save(new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false));
	}
}
