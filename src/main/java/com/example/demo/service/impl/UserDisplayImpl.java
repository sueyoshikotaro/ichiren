/* SQL エラー  (1054): Unknown column 'te66667777' in 'where clause' *//* SQL エラー  (1054): Unknown column 'te66667777' in 'where clause' */
package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.form.UserDisplay;
import com.example.demo.repository.UserDisplayCrudRepository;
import com.example.demo.service.UserDisplayServiceInterface;

public class UserDisplayImpl implements UserDisplayServiceInterface {

	@Autowired
	UserDisplayCrudRepository userCrudRepo;

	/*
	 * 向江
	 * ユーザ一覧
	 */
	@Override
	public Iterable<UserDisplay> userList() {

		return userCrudRepo.userList();
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
	public void PassReset(String user_id) {

		userCrudRepo.PassReset(user_id);
	}

	/*
	 * 向江
	 * 新規講師登録
	 * 入力したIDの重複チェックを行う
	 */
	@Override
	public boolean userIDCheck(String user_id) {

		boolean flg = userCrudRepo.existsById(user_id);

		if (!flg) {

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

	/*
	 * 向江
	 * 新規講師登録
	 * DBへ講師を登録する
	 */
	@Override
	public void InsertTeach(String user_id, String user_name, String user_pass, String school_name, String enr_year, int user_flg) {
		
		userCrudRepo.teInfoRegist(user_id, user_name, user_pass, school_name, enr_year, user_flg);
		
	}


}
