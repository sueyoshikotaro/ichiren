/* SQL エラー  (1054): Unknown column 'te66667777' in 'where clause' *//* SQL エラー  (1054): Unknown column 'te66667777' in 'where clause' */
package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.form.UserDisplay;
import com.example.demo.repository.GroupDisplayCrudRepository;
import com.example.demo.repository.UserDisplayCrudRepository;
import com.example.demo.service.UserDisplayServiceInterface;

public class UserDisplayImpl implements UserDisplayServiceInterface {

	@Autowired
	UserDisplayCrudRepository userCrudRepo;

	@Autowired
	GroupDisplayCrudRepository groupCrudRepo;
	
	/*
	 * 向江
	 * ユーザ一覧
	 */
	@Override
	public Iterable<UserDisplay> userList(int school_id) {
		return userCrudRepo.userList(school_id);
	}

	/**
	 * 湊原
	 * 絞り込み検索結果
	 */
	@Override
	public Iterable<UserDisplay> userFilterList(int school_id, String est_year) {
		Iterable<UserDisplay> result = null;
		if (!est_year.equals("選択なし")) {
			result = userCrudRepo.userList(est_year);
			if (school_id != 0) {
				result = userCrudRepo.userList(school_id, est_year);
			}
		} else if (school_id != 0) {
			result = userCrudRepo.userList(school_id);
			if (!est_year.equals("選択なし")) {
				result = userCrudRepo.userList(school_id, est_year);
			}
		} else {
			result = userCrudRepo.userList(school_id, est_year);
		}
		
		return result;
	}

	/*
	 * 向江
	 * ユーザ削除
	 */
	@Override
	public void DeleteUser(String user_id) {
		userCrudRepo.DeleteUser(user_id);
	}

	/*
	 * 向江
	 * パスワード初期化
	 */
	@Override
	public void PassFormat(String user_id) {
		userCrudRepo.PassFormat(user_id);
	}

	/*
	 * 向江
	 * 新規講師登録
	 * 入力したIDの重複チェックを行う
	 */
	@Override
	public boolean userIDCheck(String user_id) {

		//講師IDの重複チェック
		String flg = userCrudRepo.selectById(user_id);
		
		//登録の可否判断
		if (flg == null) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * 向江
	 * パスワードと確認用パスワードのチェックを行う
	 */
	@Override
	public boolean userPassCheck(String user_pass, String user_rpass) {

		if (user_pass.equals(user_rpass)) {

			return true;
		} else {

			return false;
		}

	}

	@Override
	public void InsertUser(String user_id, String user_name, String user_pass, String school_id, String enr_year,
			int user_flg) {

		userCrudRepo.userRegist(user_id, user_name, user_pass, school_id, enr_year, user_flg);

	}

	/*
	 * 向江　
	 * 新規講師登録
	 * DBへ講師を登録する
	 */
	@Override
	public void InsertTeach(String user_id, String user_name, String user_pass, String school_id, String enr_year,
			int user_flg) {

		userCrudRepo.teInfoRegist(user_id, user_name, user_pass, school_id, enr_year, user_flg);

	}

	/*
	 * 向江
	 * 新規講師登録2
	 */
	@Override
	public void registerUser(String user_id, String user_name, String user_pass, String school_name, String enr_year,
			int user_flg) {

		userCrudRepo.saveAll(user_id, user_name, user_pass, school_name, enr_year, user_flg);

	}

	/*
	 * 向江
	 * 講師一覧表示
	 */
	@Override
	public Iterable<UserDisplay> teList(int school_id) {

		return userCrudRepo.teList(school_id);
	}

	/*
	 * 向江
	 * 講師情報編集
	 */
	@Override
	public void teInfoUpdate(String user_name, String school_name, String enr_year, String user_id) {

		userCrudRepo.teInfoUpdate(user_name, school_name, enr_year, user_id);

	}

	/*
	 * 向江
	 * 講師退職
	 */
	@Override
	public void teInfoDelete(String user_id) {

		userCrudRepo.teInfoDelete(user_id);

	}

	/*
	 * 坂本
	 * ユーザパスワード再設定
	 */
	@Override
	public void userPassReset(String user_id, String newPass) {

		userCrudRepo.userPassReset(user_id, newPass);

	}

	/*
	 * 坂本
	 * パスワード無効化
	 */
	@Override
	public void adminDisable(String user_id, int user_flg) {

		userCrudRepo.adminDisable(user_id, user_flg);

	}
}
