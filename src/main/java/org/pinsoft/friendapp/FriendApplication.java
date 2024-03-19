package org.pinsoft.friendapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FriendApplication {
	public static void main(String[] args) {
		SpringApplication.run(FriendApplication.class, args);
	}

}
