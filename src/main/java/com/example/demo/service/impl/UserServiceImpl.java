package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.User;
import com.example.demo.repository.UserCrudRepository;
import com.example.demo.service.UserServiceInterface;

public class UserServiceImpl implements UserServiceInterface {

	@Autowired
	UserCrudRepository userCrudRepo;
	
	
	@Override
	public Iterable<User> userList() {
		//return null;
		// TODO 自動生成されたメソッド・スタブ
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
	
	
	/**
	 * 坂本
	 * 管理者
	 * admin判定
	 */
	@Override
	public String adminCheck(String user_id, String user_pass) {
 
		if (user_id.equals("admin") && user_pass.equals("admin")) {
 
			return null;
		} else {
 
			return null;
		}
	}

}
