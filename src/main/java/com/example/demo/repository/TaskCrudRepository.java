package com.example.demo.repository;

import java.util.List;

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
	 * 学校情報詳細
	 */
	@Query("select * from task")
	public List<Task> selectTask();
}
