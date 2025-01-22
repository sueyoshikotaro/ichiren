package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.User;

public interface UserCrudRepository extends CrudRepository<User, String> {

	@Query("select user.user_id,user.user_name,user.user_pass,user.school_id,user.enr_year,user.user_flg")
	public List<User> findAll();

	/*
	 * 向江
	 * 新規講師登録
	 * userテーブルへデータを登録する
	 */
	@Modifying
	@Query("insert into user (user_id, user_name, user_pass, school_id, enr_year, user_flg) values (:user_id, :user_name,'taskdon1', (select school_id from school where school_name = :school_name), :enr_year, 1)")
	public void saveAll(String user_id, String user_name, String user_pass, String school_name, String enr_year,
			int user_flg);

	/**
	 * 再設定したパスワードを登録
	 */
	@Modifying
	@Query("update user set user.user_pass = :newPass where user.user_id = :user_id")
	public void userPassReset(String user_id, String newPass);
}
