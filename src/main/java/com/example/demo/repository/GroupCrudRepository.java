package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Teams;

public interface GroupCrudRepository extends CrudRepository<Teams, Integer> {

//	/**
//	 * 坂本
//	 * ユーザ
//	 * 所属グループ一覧
//	 */
//	@Query("select * from teams")
//	public List<Teams> deptGroupList();
}
