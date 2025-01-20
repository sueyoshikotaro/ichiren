package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Tdlist;

public interface TodoCrudRepository extends CrudRepository<Tdlist, Integer> {

	/**
	 * 湊原
	 * Todoリスト一覧表示
	 * @param group_id
	 * @return
	 */
	@Query("select * from tdlist where user_id=:user_id")
	List<Tdlist> selectTodoList(String user_id);

	/**
	 * 湊原
	 * ラジオボタンで取得された列のデータを取得する
	 * @param tdlist_id
	 * @return
	 */
	@Query("select * from tdlist where tdlist_id=:tdlist_id;")
	List<Tdlist> selectTodo(int tdlist_id);

	
	/**
	 * ToDoを追加する
	 * 湊原
	 * @param user_id
	 * @param tdlist_content
	 * @param importance
	 */
	@Modifying
	@Query("insert into tdlist(user_id, tdlist_content, importance) values(:user_id, :tdlist_content, :importance)")
	public void registerTodo(String user_id, String tdlist_content, String importance);
	
	/**
	 * TodDoを更新する
	 * 湊原
	 */
	@Modifying
	@Query("update tdlist set tdlist_content=:tdlist_content, importance=:importance where tdlist_id=:tdlist_id")
	public void updateTodo(int tdlist_id, String tdlist_content, String importance);
	
	/**
	 * TodDoを削除する
	 * 湊原
	 */
	@Modifying
	@Query("delete from tdlist where tdlist_id=:tdlist_id")
	public void deleteTodo(int tdlist_id);

}
