package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.Todo;
import com.example.demo.repository.TodoCrudRepository;
import com.example.demo.service.TodoServiceInterface;

public class TodoServiceImpl implements TodoServiceInterface {

	@Autowired
	TodoCrudRepository repo;
	
	/**
	 * 湊原
	 * ToDoリスト一覧表示
	 */
	@Override
	public List<Todo> selectTodoList(String user_id) {
		return repo.selectTodoList(user_id);
	}

}
