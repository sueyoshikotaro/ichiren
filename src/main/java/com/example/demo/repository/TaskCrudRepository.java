package com.example.demo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Task;
import com.example.demo.form.TaskForm;

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
	@Query("select task.* ,user.user_name from task inner join user on task.user_id=user.user_id inner join teams on task.group_id=teams.group_id where teams.group_id=:group_id and task_flg= 1;")
	public List<TaskForm> selectTask(int group_id);

	@Query("select task.* ,user.user_name from task inner join user on task.user_id=user.user_id inner join teams on task.group_id=teams.group_id where teams.group_id=:group_id and task_flg= 1 and user_name=:user;")
	public List<TaskForm> selectTask(String user, int group_id);

	/**
	 * タスク詳細データ取得
	 * 湊原
	 * @param task_id
	 * @return
	 */
	@Query("select * ,user.user_name from task inner join user on task.user_id=user.user_id inner join teams on task.group_id=teams.group_id where teams.group_id=:group_id and task_flg= 1 and task_id = :task_id;")
	public List<TaskForm> selectTaskDetails(int task_id, int group_id);
	
	/**
	 * 湊原
	 * 担当者検索
	 */
	@Query("select user.user_name from teams inner join user_detail on teams.group_id = user_detail.group_id "
			+ "inner join user on user.user_id = user_detail.user_id where user_detail.group_id=:group_id;")
	public Iterable<String> selectTaskByUser(int group_id);

	/**
	 * 湊原
	 * タスク登録
	 */
	@Modifying
	@Query("insert into task(task_category, task_name, task_content, task_status, start_date, end_date, task_priority, task_level, task_weight, progress, task_flg, user_id, group_id)"
			+ "	VALUES(:task_category, :task_name, :task_content, :task_status, :start_date, :end_date, :task_priority,:task_level, :task_weight, 0, 1, (SELECT user_id FROM user WHERE user_name=:user_name), :group_id);")
	public boolean registerTask(String task_category, String task_name, String task_content, String task_status,
			Date start_date, Date end_date, int task_priority, int task_level, int task_weight, String user_name, int group_id);

	/**
	 * タスク編集
	 * @param task_id
	 * @param task_category
	 * @param task_name
	 * @param task_content
	 * @param task_status
	 * @param task_priority
	 * @param task_weight
	 * @param user_name
	 * @return
	 */
	@Modifying
	@Query("update task set task_name=:task_name, task_category=:task_category, task_content=:task_content, task_priority=:task_priority,task_weight=:task_weight,user_id=(SELECT user_id FROM user WHERE user_name = :user_name) where task_id=:task_id")
	public boolean updateTask(int task_id, String task_category, String task_name, String task_content,
			int task_priority, int task_weight, String user_name);

	/**
	 * タスクフラグ更新(削除)
	 * @param task_id
	 */
	@Modifying
	@Query("update task set task_flg = 0 where task_id = :task_id")
	public void updateFlg(int task_id);

	/**
	 * 湊原
	 * スコアを取得するメソッド
	 */
	@Query("select score from user_detail where user_detail.user_id=(select user_id from user where user_name=:user_name) and group_id=:group_id;")
	public int selectScore(String user_name, int group_id);

	/**
	 * 湊原
	 * タスクの登録、編集、削除に伴うスコアの更新
	 */
	@Modifying
	@Query("update user_detail set score=:score where user_id=(select user_id from user where user_name=:user_name) and group_id=:group_id;")
	public void updateScore(int score, String user_name, int group_id);
	
	/**
	 * 湊原
	 * タスクの進捗を更新するメソッド
	 */
	@Modifying
	@Query("update task set progress=:progress, task_status=:taskstatus where task_id=:task_id;")
	public void updateProgress(int task_id, int progress, String taskstatus);

	/**
	 * 湊原
	 * ユーザが所属しているグループのカテゴリだけを取得(絞り込み用)
	 * @param group_id
	 * @return
	 */
	@Query("select distinct task_category from task where group_id=:group_id and task_flg= 1;")
	public List<TaskForm> selectCategory(int group_id);

	
}
