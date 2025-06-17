package com.waildevil.job_board_api;

import com.waildevil.job_board_api.entity.Role;
import com.waildevil.job_board_api.entity.User;
import com.waildevil.job_board_api.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JobBoardApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobBoardApiApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository) {
		return args -> {
			if (userRepository.count() == 0) {
				userRepository.save(User.builder()
						.name("Admin User")
						.email("admin@jobboard.com")
						.password("test123") // You'll hash this later
						.role(Role.ADMIN)
						.build());
			}
		};
	}


}
