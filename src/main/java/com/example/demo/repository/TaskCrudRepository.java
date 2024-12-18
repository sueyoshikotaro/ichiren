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
	 * タスク表示
	 */
	@Query("select * from task where task_flg=1")
	public List<Task> selectTask();

	/**
	 * 湊原
	 * 担当者検索
	 * 今の段階ではグループIDを判別できないので仮で値を設定しています。
	 * 所属グループ一覧で選択したグループのIDを取得してください
	 */
	@Query("select user.user_name from teams inner join user_detail on teams.group_id = user_detail.group_id "
			+ "inner join user on user.user_id = user_detail.user_id where user_detail.group_id=1;")
	public Iterable<Task> selectTaskByUser();

	/**
	 * 湊原
	 * タスク登録
	 */
	@Modifying
	@Query("insert into task values (:task_id, :taskCategory, :taskName, :taskContent, :taskStatus, :startDate, :endDate, :taskPriority, :taskLevel, :taskWeight, :progress, :taskFlg, :user_id, :group_id)")
	public void registerTask(Task task);
}
