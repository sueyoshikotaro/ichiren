package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.form.UserDisplay;

public interface UserDisplayCrudRepository extends CrudRepository<UserDisplay, String>  {

	/*
	 * 向江
	 * ユーザ一覧表示
	 */
	@Query("select u.user_id,u.user_name,'****' as user_pass,s.school_name as school_name,u.enr_year from user u inner join school s on u.school_id = s.school_id")
	public List<UserDisplay> userList();
}
