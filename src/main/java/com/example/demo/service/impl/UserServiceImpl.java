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

		boolean id_flg = userCrudRepo.existsById(user_id);

		if (id_flg) {

			return true;
		} else {

			return false;
		}
	}

	/**
	 * 坂本
	 * ユーザ
	 * 入力したパスワードのチェック
	 */
	@Override
	public boolean userPassCheck(String user_pass) {

		boolean pass_flg = userCrudRepo.existsById(user_pass);

		if (pass_flg) {

			return true;
		} else {

			return false;
		}
	}

	/*
	 * 坂本
	 * ユーザ
	 * パスワード再設定
	 */
	@Override
	public void userPassReset(String user_pass) {

		userCrudRepo.userPassReset(user_pass);
	}
}
