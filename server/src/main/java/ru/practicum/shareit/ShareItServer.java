package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShareItServer {
	public static final String USER_ID_HEADER = "X-Sharer-User-Id";

	public static void main(String[] args) {
		SpringApplication.run(ShareItServer.class, args);
	}

}
