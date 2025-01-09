package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Task;

public interface TaskReqCrudRepository extends CrudRepository<Task, Integer>{
	/**
	 * 独自のSQL文<br>
	 * SELCT文以外は「@Modifying」を付与
	 * 
	 */
	
	/**
	 * 湊原
	 * タスク承認、未承認一覧
	 */
	@Query("select task.* ,user.user_name from task inner join user on task.user_id=user.user_id where task_flg= 1 order by task_id desc;")
	public List<Task> selectTask();
}
