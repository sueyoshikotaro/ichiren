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
	 * ID・パスワードをチェック
	 */
	@Override
	public boolean userIdCheck(String user_id, String user_pass) {

		boolean id_flg = userCrudRepo.existsById(user_id);

		boolean pass_flg = userCrudRepo.existsById(user_pass);

		if (id_flg && pass_flg) {

			return true;
		} else {

			return false;
		}
	}

	/**
	 * 坂本
	 * 管理者
	 * ID・パスワードのチェック
	 */
	@Override
	public boolean adminIdCheck(String user_id, String user_pass) {

		boolean id_flg = userCrudRepo.existsById(user_id);

		boolean pass_flg = userCrudRepo.existsById(user_pass);

		if (id_flg && pass_flg) {

			return true;
		} else {

			return false;
		}
	}

	/*
	 * 坂本
	 * ユーザ
	 * デフォルトパスワードチェック
	 */
	@Override
	public boolean defaultPassCheck(String user_pass) {

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
	public void userPassReset(String user_id, String newPass) {

		userCrudRepo.userPassReset(user_id, newPass);
	}

	/*
	 * 坂本
	 * 管理者
	 * パスワード無効化
	 */
	@Override
	public void adminDisable(String user_id, int user_flg) {
		
		userCrudRepo.adminDisable(user_id, user_flg);
	}
}
