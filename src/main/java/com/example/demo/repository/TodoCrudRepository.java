package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Todo;

public interface TodoCrudRepository extends CrudRepository<Todo, Integer> {

	/**
	 * 湊原
	 * Todoリスト一覧表示
	 * @param group_id
	 * @return
	 */
	@Query("select * from tdlist where user_id=:user_id")
	List<Todo> selectTodoList(String user_id);

}
