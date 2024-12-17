package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.School;

/**
 * クラッドメソッド用のリポジトリ
 */

public interface SchoolCrudRepository extends CrudRepository<School, Integer> {

	/**
	 * 独自のSQL文<br>
	 * SELCT文以外は「@Modifying」を付与
	 * @param school_id
	 * @param scholl_add
	 * @param school_name
	 * @param room_id
	 * @param room_name
	 * @param pc_flg
	 * @param hall
	 * @param floor
	 * @param school_id
	 */

	/**
	 * 末吉
	 * 学校情報詳細
	 */
	@Query("select * from school inner join room on school.school_id = room.school_id")
	public List<School> selectSchoolDetails();
	
	/**
	 * 末吉
	 * 教室検索
	 */
	@Query("select * from school inner join room on school.school_id = room.school_id where room.room_name Like '%' || :room_name || '%'")
	public List<School> serchRoom(String room_name);

}
