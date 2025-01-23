/* SQL エラー  (1054): Unknown column 'te66667777' in 'where clause' *//* SQL エラー  (1054): Unknown column 'te66667777' in 'where clause' */
package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.form.Room;
import com.example.demo.form.UserDisplay;
import com.example.demo.repository.UserDisplayCrudRepository;
import com.example.demo.repository.UserViewCrudRepository;
import com.example.demo.service.UserDisplayServiceInterface;

public class UserDisplayImpl implements UserDisplayServiceInterface {

	@Autowired
	UserDisplayCrudRepository userCrudRepo;

	@Autowired
	UserViewCrudRepository userViewCrudRepo;

	/*
	 * 向江
	 * ユーザ一覧
	 */
	@Override
	public Iterable<UserDisplay> userList(int school_id) {

		return userCrudRepo.userList(school_id);
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

		String flg = userCrudRepo.selectById(user_id);

		if (flg != "") {
			System.out.println("重複しないユーザID");
			return true;
		} else {
			System.out.println("重複するユーザID");
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
	public void teInfoUpdate(String user_id, String user_name, String school_name, String enr_year, int user_flg) {

		userCrudRepo.teInfoUpdate(user_id, user_name, school_name, enr_year, 1);

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

	/*
	 * 坂本
	 * 居場所選択
	 */
	@Override
	public List<Room> roomSelect() {

		return userCrudRepo.roomSelect();

	}

}
