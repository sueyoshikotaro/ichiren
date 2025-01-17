package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Todo;

public interface TodoServiceInterface {

	/**
	 * 湊原
	 * ToDoリスト一覧表示
	 */
	public List<Todo> selectTodoList(String user_id);
}
