package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.form.SchoolDisplay;

public interface SchoolDisplayCrudRepository extends CrudRepository<SchoolDisplay, Integer> {
	
	/**
	 * 末吉
	 * 学校情報詳細
	 */
	@Query("select * from school inner join room on school.school_id = room.school_id order by room.room_name Desc")
	public List<SchoolDisplay> selectSchoolDetails();
}
