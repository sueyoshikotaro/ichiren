package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.form.UserDisplay;

public interface UserDisplayCrudRepository extends CrudRepository<UserDisplay, String> {

	/**
	 * 末吉
	 * 講師情報登録時に同じIDが存在するかチェック
	 */
	@Query("select user_id from user where user_id = :user_id")
	public String selectById(String user_id);

	/*
	 * 向江
	 * ユーザ一覧表示用のSQL
	 */
	//学校名だけ選択されている場合
	@Query("select u.user_id,u.user_name,'****' as user_pass,s.school_name as school_name,u.enr_year,u.school_id from user u inner join school s on u.school_id = s.school_id where user_flg = 1 and user_id like '%st%' and u.school_id = :school_id")
	public List<UserDisplay> userList(int school_id);
	//学校名と年度がどちらも選択されている場合
	@Query("select u.user_id,u.user_name,'****' as user_pass,s.school_name as school_name,u.enr_year,u.school_id from user u inner join school s on u.school_id = s.school_id"
			+ " where user_flg = 1 and user_id like '%st%' and u.school_id = :school_id and Year(u.enr_year) = :enr_year")
	public Iterable<UserDisplay> userList(int school_id, String enr_year);
	//年度だけ選択されている場合
	@Query("select u.user_id,u.user_name,'****' as user_pass,s.school_name as school_name,u.enr_year,u.school_id from user u inner join school s on u.school_id = s.school_id"
			+ " where user_flg = 1 and user_id like '%st%' and Year(u.enr_year) = :enr_year")
	public Iterable<UserDisplay> userList(String enr_year);
	

	/*
	 * 向江
	 * ユーザ削除用のSQL
	 */
	@Modifying
	@Query("update user set user_flg = 0 where user_id = :user_id")
	public void DeleteUser(String user_id);

	/*
	 * 向江
	 * パスワード初期化用のSQL
	 */
	@Modifying
	@Query("update user set user_pass = 'taskdon1' where user_id = :user_id")
	public void PassFormat(String user_id);

	/*
	 * 向江
	 * パスワード再設定
	 */
	@Modifying
	@Query("update user set user_pass = :user_pass where user_id = :user_id")
	public void passReset(String user_id, String user_pass);

	/**
	 * 末吉
	 * 新規ユーザ登録
	 */
	@Modifying
	@Query("insert into user (user_id, user_name, user_pass, school_id, enr_year, user_flg) values (:user_id, :user_name, :user_pass, :school_id, :enr_year, :user_flg)")
	public void userRegist(String user_id, String user_name, String user_pass, String school_id, String enr_year,
			int user_flg);

	/*
	 * 向江
	 * 講師情報登録用のSQL
	 */
	@Modifying
	@Query("insert into user (user_id, user_name, user_pass, school_id, enr_year, user_flg) VALUES (:user_id, :user_name, 'taskdon1', (select school_id FROM school WHERE school_name = :school_name), :enr_year, 1)")
	public void teInfoRegist(String user_id, String user_name, String user_pass, String school_id, String enr_year,
			int user_flg);
	
	/**
	 * 坂本
	 * adminログイン時の講師登録
	 * id,passチェック
	 */
//	@Query("select count(*) from user where user_id = :user_id and user_pass = :user_pass and user_id like '%te%'")
//	public int adminTeachCheck(String user_id, String user_pass);

	/*
	 * 向江
	 * 講師一覧表示用のSQL(stアカウント以外)
	 */
	@Query("select u.user_id,u.user_name,'****' as user_pass,s.school_name as school_name,u.enr_year from user u inner join school s on u.school_id = s.school_id where user_flg = 1 and user_id not like '%st%' and u.school_id = :school_id")
	public List<UserDisplay> teList(int school_id);

	/*
	 * 向江
	 * 講師情報編集用のSQL
	 */
	@Modifying
	@Query("update user set user_name = :user_name, school_id = (select school_id from school where school_name = :school_name), enr_year = :enr_year where user_id = :user_id")
	public void teInfoUpdate(String user_name, String school_name, String enr_year, String user_id);

	/*
	 * 向江
	 * 講師退職用のSQL
	 */
	@Modifying
	@Query("update user set user_flg = 0 where user_id = :user_id")
	public void teInfoDelete(String user_id);

	/*
	 * 向江
	 * userテーブルにデータを登録する
	 */
	@Modifying
	@Query("insert into user (user_id, user_name, user_pass, school_id, enr_year, user_flg) values (:user_id, :user_name,'taskdon1', (select school_id from school where school_name = :school_name), :enr_year, 1)")
	public void saveAll(String user_id, String user_name, String user_pass, String school_name, String enr_year,
			int user_flg);

	/**
	 * 坂本
	 * ユーザパスワード再設定
	 */
	@Modifying
	@Query("update user set user.user_pass = :newPass where user.user_id = :user_id")
	public void userPassReset(String user_id, String newPass);

	/**
	 * 坂本
	 * パスワード無効化
	 */
	@Modifying
	@Query("update user set user.user_flg = 0 where user.user_id = :user_id")
	public void adminDisable(String user_id, int user_flg);

}
