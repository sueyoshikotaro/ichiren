package com.example.demo.service;

import com.example.demo.form.UserDisplay;

public interface UserDisplayServiceInterface {
	
	// ユーザ一覧表示
	public Iterable<UserDisplay> userList();
	
	// ユーザ削除
	public void DeleteUser(String user_id);
	
	// パスワード初期化
	public void PassReset(String user_id);

	
}
