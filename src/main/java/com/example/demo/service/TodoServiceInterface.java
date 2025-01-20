package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Tdlist;

public interface TodoServiceInterface {

	 //ToDoリスト一覧表示
	public List<Tdlist> selectTodoList(String user_id);

	//選択されたToDoリスト表示検索
	public List<Tdlist> selectTodo(int tdlist_id);

	//ToDoを追加する
	public void todoRegister(String user_id, String tdlist_content, String importance);
	
	//TodDoを更新する
	public void todoUpdate(int tdlist_id, String tdlist_content, String importance);
	
	//TodDoを削除する
	public void todoDelete(int tdlist_id);
}
