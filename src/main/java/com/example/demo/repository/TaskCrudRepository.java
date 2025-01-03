package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Task;

public interface TaskCrudRepository extends CrudRepository<Task, Integer> {
	/**
	 * 独自のSQL文<br>
	 * SELCT文以外は「@Modifying」を付与
	 * 
	 */

	/**
	 * 湊原
	 * タスク一覧表示
	 */
	@Query("select task.* ,user.user_name from task inner join user on task.user_id=user.user_id where task_flg= 1;")
	public List<Task> selectTask();
	
	@Query("select task.* ,user.user_name from task inner join user on task.user_id=user.user_id where task_flg= 1 and user_name=:user;")
	public List<Task> selectTask(String user);

	/**
	 * 湊原
	 * 担当者検索
	 * 今の段階ではグループIDを判別できないので仮で値を設定しています。
	 * 所属グループ一覧で選択したグループのIDを取得してください
	 */
	@Query("select user.user_name from teams inner join user_detail on teams.group_id = user_detail.group_id "
			+ "inner join user on user.user_id = user_detail.user_id where user_detail.group_id=1;")
	public Iterable<String> selectTaskByUser();

	/**
	 * 湊原
	 * タスク登録
	 */
	@Modifying
	@Query("insert into task(task_category, task_name, task_content, task_status, start_date, end_date, task_priority, task_level, task_weight, progress, task_flg, user_id, group_id)"
			+ " select distinct :task_category, :task_name, :task_content, :task_status, '2024-12-19', '2024-12-30', :task_priority, :task_level, :task_weight, 0, 1, user.user_id, 1 from task"
			+ " inner join user on task.user_id=user.user_id where user_name=:user_name;")
	public boolean registerTask(String task_category, String task_name, String task_content, String task_status,
			String start_date, String end_date, String task_priority, String task_level, String task_weight, String user_name, String group_id);
}
