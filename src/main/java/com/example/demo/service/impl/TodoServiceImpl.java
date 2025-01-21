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

	/**
	 * 湊原
	 * ToDoを追加する
	 */
	@Override
	public void todoRegister(String user_id, String tdlist_content, String importance) {
		repo.registerTodo(user_id, tdlist_content, importance);		
	}

	/**
	 * 湊原
	 * TodDoを更新する
	 */
	@Override
	public void todoUpdate(int tdlist_id, String tdlist_content, String importance) {
		repo.updateTodo(tdlist_id, tdlist_content, importance);
		
	}

	/**
	 * 湊原
	 * TodDoを削除する
	 */
	@Override
	public void todoDelete(int tdlist_id) {
		repo.deleteTodo(tdlist_id);
	}

	/**
	 * 湊原
	 * 選択されたToDoリストを完了にする
	 */
	@Override
	public void todoUpFlg(int tdlist_id,int tdlist_flg) {
		repo.updateTodoFlg(tdlist_id,tdlist_flg);
	}

}
