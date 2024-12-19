package com.example.demo.service;

import com.example.demo.form.UserDisplay;

public interface UserDisplayServiceInterface {

	// ユーザ一覧表示
	public Iterable<UserDisplay> userList();

	// ユーザ削除
	public void DeleteUser(String user_id);

	// パスワード初期化
	public void PassReset(String user_id);

	// IDチェック
	public boolean userIDCheck(String user_id);
	
	// passチェック
	public boolean userPassCheck(String user_pass, String user_rpass);

	
	// 新規講師登録
	public void InsertTeach(String user_id, String user_name, String user_pass, String school_name, String enr_year, int user_flg); 
		

}
