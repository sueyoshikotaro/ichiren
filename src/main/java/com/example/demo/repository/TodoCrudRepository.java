package com.example.demo.repository;

import java.util.List;

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

}
