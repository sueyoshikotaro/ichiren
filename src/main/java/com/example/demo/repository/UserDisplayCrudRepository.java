package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.form.UserDisplay;

public interface UserDisplayCrudRepository extends CrudRepository<UserDisplay, String>  {

	/*
	 * 向江
	 * ユーザ一覧表示用のSQL
	 */
	@Query("select u.user_id,u.user_name,'****' as user_pass,s.school_name as school_name,u.enr_year from user u inner join school s on u.school_id = s.school_id where user_flg = 1")
	public List<UserDisplay> userList();
	
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
	public void PassReset(String user_id);

	/*
	 * 向江
	 * 講師情報登録用のSQL
	 */
	@Modifying
	@Query("insert into user(user_id,user_name,user_pass,school_id,enr_year,user_flg) values(:user_id,:user_name,'taskdon1',(select school_id from school where school_name = :school_name),:enr_year,1)")
	public void teInfoRegist(String user_id, String user_name, String user_pass, String school_name, String enr_year, int user_flg);

}
