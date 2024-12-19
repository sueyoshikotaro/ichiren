package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.User;
import com.example.demo.repository.UserCrudRepository;
import com.example.demo.service.UserServiceInterface;

public class UserServiceImpl implements UserServiceInterface {

	@Autowired
	UserCrudRepository userCrudRepo;
	
	@Override
	public List<User> userList() {

		return userCrudRepo.findAll();
	}

	/*
	 * 坂本
	 * ユーザ
	 * 入力したIDの重複をチェック
	 */
	@Override
	public boolean userIdCheck(String user_id) {

		boolean flg = userCrudRepo.existsById(user_id);

		if (flg) {

			return true;
		} else {

			return false;
		}
	}
}
