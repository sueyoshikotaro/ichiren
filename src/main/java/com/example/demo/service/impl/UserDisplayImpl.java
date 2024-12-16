package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.form.UserDisplay;
import com.example.demo.repository.UserDisplayCrudRepository;
import com.example.demo.service.UserDisplayServiceInterface;

public class UserDisplayImpl implements UserDisplayServiceInterface {


	@Autowired
	UserDisplayCrudRepository userCrudRepo;
	
	@Override
	public Iterable<UserDisplay> userList() {

		//return null;

		return userCrudRepo.userList();
	}

}
