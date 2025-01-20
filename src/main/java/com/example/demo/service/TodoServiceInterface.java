package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Tdlist;

public interface TodoServiceInterface {

	/**
	 * 湊原
	 * ToDoリスト一覧表示
	 */
	public List<Tdlist> selectTodoList(String user_id);

	//選択されたToDoリスト表示検索
	public List<Tdlist> selectTodo(int tdlist_id);
}
