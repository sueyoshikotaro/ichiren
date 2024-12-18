package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Task;

public interface TaskServiceInterface {
	//タスク一覧表示
	public List<Task> taskDisplayList();
	
	//タスク登録
	public void taskRegister(Task task);
		
	//タスク担当者検索
	public Iterable<Task> taskUserSearch();
}
