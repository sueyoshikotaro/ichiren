package com.example.demo.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Teams;

public interface GroupDisplayCrudRepository extends CrudRepository<Teams, Integer>{

	/*
	 * 向江
	 * 管理者_グループ一覧表示用のSQL
	 */
	@Query("select * from teams where group_flg = 1")
	public List<Teams> groupList();
	
	
	/*
	 * 管理者_グループ一覧
	 * グループ発足年度で絞り込みをする
	 */
	@Query("select * from teams where group_flg = 1 and est_year = :estYear")
	public List<Teams> groupList(String estYear);

	/*
	 * 管理者_グループ一覧
	 * 年だけ抽出
	 */
	@Query("select est_year, YEAR(est_year) FROM teams")
	public Collection<Teams> findDistinctEstYear();
}
