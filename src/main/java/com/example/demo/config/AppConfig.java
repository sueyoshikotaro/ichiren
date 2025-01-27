package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.service.ChatServiceInterface;
import com.example.demo.service.GroupDisplayServiceInterface;
import com.example.demo.service.NoticeServiceInterface;
import com.example.demo.service.SchoolDisplayServiceInterface;
import com.example.demo.service.TaskServiceInterface;
import com.example.demo.service.TodoServiceInterface;
import com.example.demo.service.UserDisplayServiceInterface;
import com.example.demo.service.impl.ChatServiceImpl;
import com.example.demo.service.impl.GroupDisplayImpl;
import com.example.demo.service.impl.NoticeServiceImpl;
import com.example.demo.service.impl.SchoolDisplayImpl;
import com.example.demo.service.impl.TaskServiceImpl;
import com.example.demo.service.impl.TodoServiceImpl;
import com.example.demo.service.impl.UserDisplayImpl;

@Configuration
public class AppConfig {

	@Bean(name = "schoolDisplayService")
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

	@Bean(name = "groupService")
	GroupDisplayServiceInterface groupService() {
		return new GroupDisplayImpl();
	}

	@Bean(name = "todoService")
	TodoServiceInterface todoService() {
		return new TodoServiceImpl();
	}

	@Bean(name = "NoticeService")
	NoticeServiceInterface noticeService() {
		return new NoticeServiceImpl();
	}
	
	@Bean(name = "ChatService")
	ChatServiceInterface chatService() {
		return new ChatServiceImpl();
	}

}
