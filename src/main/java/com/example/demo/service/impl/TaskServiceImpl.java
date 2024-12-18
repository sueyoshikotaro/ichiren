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
	public List<Task> taskDisplayList() {

		return repo.selectTask();
	}

	@Override
	public void taskRegister(Task task) {
		repo.registerTask(task);
	}

	@Override
	public Iterable<Task> taskUserSearch() {
//		
		return repo.selectTaskByUser();
	}

}
