package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Task;

public interface TaskServiceInterface {
	//タスク一覧表示
	public List<Task> taskDisplayList();
	
	//タスク登録
	public void taskRegister(String task_category, String task_name, String task_content, String string,
			String start_date, String end_date, String task_priority, String task_level, String task_weight, String user_id, String group_id);
		
	//タスク担当者検索
	public Iterable<Task> taskUserSearch();
}
