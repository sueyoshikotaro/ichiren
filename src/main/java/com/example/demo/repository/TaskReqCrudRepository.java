package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Task;
import com.example.demo.entity.TaskReqView;

public interface TaskReqCrudRepository extends CrudRepository<Task, Integer>{
	/**
	 * 独自のSQL文<br>
	 * SELCT文以外は「@Modifying」を付与
	 * 
	 */
	
	/*
	 * 向江
	 * タスク未承認一覧
	 */
	@Query("select u.user_name as user_name,r.req_name,r.add_date from task_req r inner join user u on r.user_id = u.user_id where request_flg = 0 order by request_id;")
	public List<TaskReqView> selectTaskUnapproved();
	
	
	/*
	 * 向江
	 * タスク未承認確認
	 */
	@Query("update task_req set request_flg = 1 where request_id = :request_id;")
	public boolean updateTaskUnapproved(int request_id);
}
