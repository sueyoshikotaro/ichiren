package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.User;

public interface UserServiceInterface {

	//サービスメソッド

	// ユーザ一覧表示
	public List<User> userList();
	// return userCrudRepository.findAll()

	/**
	 * 坂本
	 * ユーザ
	 * ID・パスワードをチェック
	 */
	public boolean userIdCheck(String user_id, String user_pass);

	/**
	 * 坂本
	 * 管理者
	 * ID・パスワードをチェック
	 */
	public boolean adminIdCheck(String user_id, String user_pass);

	/**
	 * 坂本
	 * 管理者
	 * デフォルトパスワードをチェック
	 */
	public boolean defaultPassCheck(String user_pass);

	/**
	 * 坂本
	 * ユーザ
	 * パスワード再設定
	 */
	public void userPassReset(String user_pass);
}
