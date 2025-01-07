package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskCrudRepository;
import com.example.demo.service.TaskServiceInterface;

public class TaskServiceImpl implements TaskServiceInterface {
	//湊原
	@Autowired
	TaskCrudRepository repo;

	/**
	 * 湊原
	 * タスク一覧表示
	 */
	@Override
	public List<Task> taskDisplayList(String user) {
		List<Task> result;
		if(user.equals("all")) {
			result = repo.selectTask();
		}else {
			result = repo.selectTask(user);
		}
		return result;
	}
	
	/**
	 * 湊原
	 * タスク登録
	 */
	@Override
	public void taskRegister(String task_category, String task_name, String task_content, String str,
			String start_date, String end_date, String task_priority, String task_level, String task_weight, String user_name, String group_id) {
		
		
		repo.registerTask(task_category, task_name, task_content, str,
				start_date, end_date, task_priority, task_level, task_weight, user_name, group_id);
		
	}

	@Override
	public Iterable<String> taskUserSearch() {
//		
		return repo.selectTaskByUser();
	}

}
