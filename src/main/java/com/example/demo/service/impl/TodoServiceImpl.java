package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.Tdlist;
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
	public List<Tdlist> selectTodoList(String user_id) {
		return repo.selectTodoList(user_id);
	}

	/**
	 * 湊原
	 * 選択されたToDoリスト表示検索
	 */
	@Override
	public List<Tdlist> selectTodo(int tdlist_id) {
		return repo.selectTodo(tdlist_id);
	}

}
