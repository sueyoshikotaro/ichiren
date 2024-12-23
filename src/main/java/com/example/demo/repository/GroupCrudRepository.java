package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Teams;

public interface GroupCrudRepository extends CrudRepository<Teams, Integer> {

	/**
	 * 坂本
	 * ユーザ
	 * 所属グループ一覧
	 */
	@Query("select group_name,genre,work_status from teams inner join user_detail on teams.group_id = user_detail.group_id")
	public List<Teams> deptGroupList();
}
