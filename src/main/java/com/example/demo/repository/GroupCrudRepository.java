package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.form.GroupDisplay;

public interface GroupCrudRepository extends CrudRepository<GroupDisplay, Integer> {

	/**
	 * 坂本
	 * ユーザ
	 * 所属グループ一覧
	 */
	@Query("select user_detail.group_id,group_name,genre,user_roll from teams inner join user_Detail on teams.group_id = user_Detail.group_id where user_Detail.user_id = :user_id")
	public List<GroupDisplay> deptGroupList(String user_id);
}
