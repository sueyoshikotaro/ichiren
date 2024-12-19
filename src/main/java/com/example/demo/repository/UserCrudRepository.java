package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.User;

public interface UserCrudRepository extends CrudRepository<User, String> {

	@Query("select user.user_id,user.user_name,user.user_pass,user.school_id,user.enr_year,user.user_flg")
	public List<User> findAll();

	/**
	 * 再設定したパスワードを登録
	 */
	@Modifying
	@Query("update user set user_pass = :user_pass where user_id = :user_id")
	public void passReset(String user_id, String user_pass);
}
