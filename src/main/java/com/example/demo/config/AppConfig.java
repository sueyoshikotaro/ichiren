package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.service.SchoolServiceInterface;
import com.example.demo.service.UserListServiceInterface;
import com.example.demo.service.impl.SchoolServiceImpl;
import com.example.demo.service.impl.UserListServiceImpl;

@Configuration
public class AppConfig {

	@Bean(name = "schoolService")
	SchoolServiceInterface schoolService() {
		return new SchoolServiceImpl();
	}
	
	
	@Bean(name="userListService")
	UserListServiceInterface userListServiceInterface(){
		return new UserListServiceImpl();
	}
}
