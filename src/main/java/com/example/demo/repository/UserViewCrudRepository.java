package com.example.demo.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.UserView;

public interface UserViewCrudRepository extends CrudRepository<UserView, String>{

	@Query("insert into user (user_id, user_name, user_pass, school_id, enr_year, user_flg) VALUES (:user_id, :user_name, 'taskdon1', (select school_name FROM school WHERE school_id = :school_id), :enr_year, 1)")
	public void save(String user_id, String user_name, String user_pass, String school_name, String enr_year, int user_flg);
	

}
