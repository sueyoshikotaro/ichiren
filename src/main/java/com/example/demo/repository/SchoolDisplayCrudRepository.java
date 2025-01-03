package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.form.SchoolDisplay;

public interface SchoolDisplayCrudRepository extends CrudRepository<SchoolDisplay, Integer> {
	
	/**
	 * 末吉
	 * 学校情報詳細
	 * 独自のSQL文<br>
	 * SELCT文以外は「@Modifying」を付与
	 */
	@Query("select * from school inner join room on school.school_id = room.school_id order by room.room_name")
	public List<SchoolDisplay> selectSchoolDetails();

	@Query("select * from school inner join room on school.school_id = room.school_id where school.school_id = 1 && room.room_id = :room_id")
	public List<SchoolDisplay> schoolDetailsChange(String room_id);
	
	@Modifying
	@Query("update room set room_name = :room_name, pc_flg = :pc_flg, hall = :hall, floor = :floor, school_id = :school_id where room_id = :room_id")
	public void EditSchoolDetailsComp(String room_name, int pc_flg, String hall, String floor, int school_id, int room_id);

	@Modifying
	@Query("insert into room (room_name, pc_flg, hall, floor, school_id) values (:room_name, :pc_flg, :hall, :floor, :school_id)")
	public void AddSchoolDetailsComp(String room_name, int pc_flg, String hall, String floor, int school_id);
}