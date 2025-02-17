package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.School;
import com.example.demo.form.SchoolDisplay;

public interface SchoolDisplayCrudRepository extends CrudRepository<School, Integer> {

	/**
	 * 末吉
	 * 学校情報詳細
	 * 独自のSQL文<br>
	 * SELCT文以外は「@Modifying」を付与
	 */
	//
	@Query("select * from school where school_id = :school_id")
	public List<School> SchoolInfo(int school_id);
	
	@Query("select * from school inner join room on school.school_id = room.school_id where room.school_id = :school_id order by room.room_name")
	public List<SchoolDisplay> selectSchoolDetails(int school_id);

	//選択した学校情報を表示
	@Query("select * from school inner join room on school.school_id = room.school_id where school.school_id = :school_id && room.room_id = :room_id")
	public List<SchoolDisplay> schoolDetailsChange(int room_id, int school_id);

	//学校情報編集
	@Modifying
	@Query("update room set room_name = :room_name, pc_flg = :pc_flg, hall = :hall, floor = :floor, school_id = :school_id where room_id = :room_id")
	public void EditSchoolDetailsComp(String room_name, int pc_flg, String hall, String floor, int school_id, int room_id);

	//学校情報追加
	@Modifying
	@Query("insert into room (room_name, pc_flg, hall, floor, school_id) values (:room_name, :pc_flg, :hall, :floor, :school_id)")
	public void AddSchoolDetailsComp(String room_name, int pc_flg, String hall, String floor, int school_id);

	//学校情報削除
	@Modifying
	@Query("delete from room where school_id = :school_id && room_id = :room_id")
	public void DeleteSchoolDetails(int school_id, int room_id);

	//同じ教室名が登録されているか検索
	@Query("select exists (select 1 from room where room_name = :room_name && school_id = :school_id)")
	public boolean isExistRoomName(String room_name, int school_id);
}