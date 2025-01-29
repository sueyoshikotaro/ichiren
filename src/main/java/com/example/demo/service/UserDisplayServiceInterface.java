package com.example.demo.service;

import java.util.List;

import com.example.demo.form.Room;
import com.example.demo.form.UserDisplay;

public interface UserDisplayServiceInterface {

	// ユーザ一覧表示
	public Iterable<UserDisplay> userList(int school_id);

	// ユーザ削除
	public void DeleteUser(String user_id);

	// パスワード初期化
	public void PassFormat(String user_id);

	// IDチェック
	public boolean userIDCheck(String user_id);

	// passチェック
	public boolean userPassCheck(String user_pass, String user_rpass);

	//新規ユーザ登録
	public void InsertUser(String user_id, String user_name, String user_pass, String school_id, String enr_year,
			int user_flg);

	// 新規講師登録
	public void InsertTeach(String user_id, String user_name, String user_pass, String school_id, String enr_year,
			int user_flg);

	// 新規講師登録
	public void registerUser(String user_id, String user_name, String user_pass, String school_name, String enr_year,
			int user_flg);

	//新規講師登録(admin)
//	public void insertAdminTeach(String user_id, String user_name, String user_pass, String school_name,
//			String enr_year, int user_flg);

	// 講師一覧表示
	public Iterable<UserDisplay> teList(int school_id);

	// 講師編集
	public void teInfoUpdate(String user_name, String school_name, String enr_year, String user_id);

	// 講師退職
	public void teInfoDelete(String user_id);

	//ユーザパスワード再設定
	public void userPassReset(String user_id, String newPass);

	//パスワード無効化
	public void adminDisable(String user_id, int user_flg);

	//居場所選択
	public List<Room> roomSelect();

	//絞り込み検索結果
	public Iterable<UserDisplay> userFilterList(int school_id, String est_year);

}
