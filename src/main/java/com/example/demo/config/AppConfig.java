package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.service.SchoolDisplayServiceInterface;
import com.example.demo.service.SchoolServiceInterface;
import com.example.demo.service.TaskServiceInterface;
import com.example.demo.service.UserDisplayServiceInterface;
import com.example.demo.service.UserServiceInterface;
import com.example.demo.service.impl.SchoolDisplayImpl;
import com.example.demo.service.impl.SchoolServiceImpl;
import com.example.demo.service.impl.TaskServiceImpl;
import com.example.demo.service.impl.UserDisplayImpl;
import com.example.demo.service.impl.UserServiceImpl;

@Configuration
public class AppConfig {

	@Bean(name = "schoolService")
	SchoolServiceInterface schoolService() {
		return new SchoolServiceImpl();
	}
	

	@Bean(name="userListService")
	UserServiceInterface userListServiceInterface(){
		return new UserServiceImpl();
	}
	
	@Bean(name="schoolDisplayService")
	SchoolDisplayServiceInterface schoolDisplayService() {
		return new SchoolDisplayImpl();
	}
	
	@Bean(name = "userDisplayImpl")
	UserDisplayServiceInterface userDisplayService() {
		return new UserDisplayImpl();
	}
	
	@Bean(name = "taskService")
	TaskServiceInterface TaskService() {
		return new TaskServiceImpl();
	}
	
}
