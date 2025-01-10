package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Teams;
import com.example.demo.form.GroupDetailView;

public interface GroupDisplayCrudRepository extends CrudRepository<Teams, Integer> {

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
	public List<Teams> groupListYear(String estYear);
	
	
	@Query("select teams.*,school.school_name from teams inner join school on teams.school_id = school.school_id where group_flg = 1 and school.school_name = :schoolName")
	public List<Teams> groupListSchool(String schoolName);
	
	@Query("select * from teams where group_flg = 1 and genre = :Genre")
	public List<Teams> groupListGenre(String Genre);
	

	/**
	 * 湊原
	 * 絞り込み
	 * @param estYear
	 * @param schoolName
	 * @param genre
	 * @return
	 */
	@Query("select * from teams where group_flg = 1 and school_name = :schoolName")
	public List<Teams> findByCriteria(String schoolName);
	
	/*
	 * cahtGPT
	 * 管理者_グループ一覧
	 * グループ一覧
	 */
	@Query("SELECT t.group_id,t.group_name,s.school_name,t.group_flg,t.genre,t.work_status,t.all_progress,YEAR(t.est_year) AS formation_year FROM Teams t JOIN School s ON t.school_id = s.school_id WHERE (:estYear IS NULL OR YEAR(t.est_year) = :year) AND :schoolName IS NULL OR s.school_name = :schoolName AND :genre IS NULL OR t.genre = :genre")
	public List<Teams> findByCriteria(String estYear, String schoolName, String genre);

	/*
	 * 向江
	 * グループ詳細表示
	 */
	@Query("select t.group_name, t.genre, t.all_progress, u.user_name, ud.user_progress, ud.score from teams t join user_detail ud on t.group_id = ud.group_id join user u on ud.user_id = u.user_id")
	public List<GroupDetailView> groupDetail(String group_id);
}
