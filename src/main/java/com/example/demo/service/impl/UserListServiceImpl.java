package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.User;
import com.example.demo.repository.UserCrudRepository;
import com.example.demo.service.UserListServiceInterface;

public class UserListServiceImpl implements UserListServiceInterface {

	@Autowired
	UserCrudRepository userCrudRepo;
	
	
	@Override
	public Iterable<User> userList() {
		//return null;
		// TODO 自動生成されたメソッド・スタブ
		return userCrudRepo.findAll();
	}

}
