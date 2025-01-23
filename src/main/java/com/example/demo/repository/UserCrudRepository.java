package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.User;

public interface UserCrudRepository extends CrudRepository<User, String> {

	/*
	 * 坂本
	 * findAll用リポジトリ
	 */
	@Query("select user.user_id,user.user_name,user.user_pass,user.school_id,user.enr_year,user.user_flg")
	public List<User> findAll();
}
