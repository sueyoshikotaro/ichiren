package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Teams;
import com.example.demo.form.GroupDetailView;
import com.example.demo.form.GroupMemberDeleteView;
import com.example.demo.form.GroupMemberDetailView;
import com.example.demo.form.TaskForm;
import com.example.demo.form.TeamsForm;

public interface GroupDisplayCrudRepository extends CrudRepository<Teams, Integer> {

	/*
	 * 向江
	 * 管理者_グループ一覧表示用のSQL
	 */
	@Query("select t.*, s.school_name from teams t inner join school s on t.school_id = s.school_id where group_flg = 1 and t.school_id = :school_id")
	public List<TeamsForm> groupList(int school_id);

	/*
	 * 管理者_グループ一覧
	 * グループ発足年度で絞り込みをする
	 */
	@Query("select * from teams where group_flg = 1 and est_year = :estYear")
	public List<TeamsForm> groupListYear(String estYear);

	@Query("select teams.*,school.school_name from teams inner join school on teams.school_id = school.school_id where group_flg = 1 and school.school_name = :schoolName")
	public List<TeamsForm> groupListSchool(String schoolName);

	@Query("select * from teams where group_flg = 1 and genre = :Genre")
	public List<TeamsForm> groupListGenre(String Genre);

	/**
	 * 湊原
	 * 絞り込み
	 * @param estYear
	 * @param schoolName
	 * @param genre
	 * @return
	 */
	@Query("select * from teams where group_flg = 1 and school_name = :schoolName")
	public List<TeamsForm> findByCriteria(String schoolName);

	/*
	 * 向江
	 * グループ詳細表示
	 */
	@Query("select t.group_id, t.group_name, t.genre, t.all_progress, u.user_name, ud.user_progress, ud.score, ud.user_roll, u.user_id, u.school_id from teams t join user_detail ud on t.group_id = ud.group_id join user u on ud.user_id = u.user_id where t.group_id = :group_id")
	public List<GroupDetailView> groupDetail(String group_id);

	/*
	 * 向江
	 * グループメンバ詳細表示
	 */
	@Query("select u.user_name, t.user_id, ud.score, ud.user_progress, t.task_name, t.task_priority, t.progress, ud.group_id from task t join user u on t.user_id = u.user_id join user_detail ud on u.user_id = ud.user_id where u.user_id = :user_id and t.group_id = :group_id")
	public List<GroupMemberDetailView> groupMemberDetail(String user_id, String group_id);

	/*
	 * 向江
	 * タスク詳細表示
	 */
	@Query("select t.task_id, t.task_name, t.task_priority, t.task_category, t.progress, t.start_date, t.end_date, t.task_content, t.task_weight from task t where t.task_name = :task_name")
	public List<TaskForm> taskDetail(String task_name);

	/**
	 * 末吉
	 * メンバが受け持つ全てのタスクを取得
	 */
	@Query("select * from task t join user_detail ud on t.group_id = ud.group_id where t.user_id = :user_id and t.group_id = :group_id")
	public List<TaskForm> taskList(String user_id, String group_id);
	
	/*
	 * 向江
	 * グループ編集
	 */
	@Modifying
	@Query("update user_detail set user_roll = 'リーダ' where group_id = :group_id")
	public void groupEdit(String group_id);

	/*
	 * 向江
	 * グループメンバ削除画面を表示するためだけのsql
	 */
	@Query("select u.user_name, t.task_id, t.task_name,t.task_content, u.user_id, t.task_id, t.group_id, t.task_weight from user u join task t on u.user_id = t.user_id where u.user_id = :user_id")
	public List<GroupMemberDeleteView> grMemDelDisp(String user_id);

	/*
	 * 向江
	 * グループメンバ削除
	 */
	@Modifying
	@Query("delete from user_detail where group_id = :group_id and user_id = :user_id")
	public void groupMemberDelete(String group_id, String user_id);
	
	/*
	 * 向江
	 * グループメンバ削除
	 * user_detailのscoreを持ってくる
	 */
	@Query("select * from user_detail where group_id = :group_id order by score asc")
	public List<GroupMemberDeleteView> membersScore(String group_id);
	
	
	/**
	 * 末吉
	 * グループ作成
	 */
	@Modifying
	@Query("insert into teams (group_name, school_id, group_flg, genre, work_status) values (:group_name, :school_id, 1, :genre, '休憩中')")
	public void groupCreate(String group_name, int school_id, String genre);

	/**
	 * 末吉
	 * 登録したグループIDを取得
	 */
	@Query("select max(group_id) from teams where school_id = :school_id")
	public int MaxGroupId(int school_id);

	/**
	 * 末吉
	 * グループ詳細
	 */
	@Modifying
	@Query("insert into user_detail (user_id, group_id, user_roll, score) values (:user_id, :group_id, :user_roll, :score)")
	public void groupDetailCreate(String user_id, int group_id, String user_roll, int score);
	
	/**
	 * 末吉
	 * 既に登録されているユーザIDを取得
	 */
	@Query("select user_id from user_detail where group_id = :group_id")
	public List<String> getExistUserIds(int group_id);
	
	/**
	 * 末吉
	 * グループ解散完了
	 */
	@Modifying
	@Query("update teams set group_flg = 0 where group_id = :group_id")
	public void groupDiss(int group_id);
	
	/**
	 * 末吉
	 * グループ編集完了
	 */
	@Modifying
	@Query("update user_detail set user_roll = :user_roll where user_id = :user_id and group_id = :group_id")
	public void groupEdit(String user_id, int group_id, String user_roll);
	
	/**
	 * 末吉
	 * タスクの再割り振り
	 */
	@Modifying
	@Query("update task set user_id = :user_id where task_id = :task_id")
	public void updateUserId(int task_id, String user_id);
	
	/**
	 * 末吉
	 * user_detailのscoreとuser_progressを更新する
	 */
	@Modifying
	@Query("update user_detail set score = :scoreResult, user_progress = :userProgressResult where user_id = :user_id and group_id = :group_id")
	public void updateScore(String user_id, String group_id, int scoreResult, int userProgressResult);
	
}
