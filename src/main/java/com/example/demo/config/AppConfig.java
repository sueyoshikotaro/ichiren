package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.service.SchoolServiceInterface;
import com.example.demo.service.impl.SchoolServiceImpl;

@Configuration
public class AppConfig {

	@Bean(name = "schoolService")
	SchoolServiceInterface schoolService() {
		return new SchoolServiceImpl();
	}
	
}
