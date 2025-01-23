package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.form.Room;

public interface RoomCrudRepository extends CrudRepository<Room, Integer> {

	/**
	 * 坂本
	 * 居場所更新
	 */
	@Query("select room.room_id, room.room_name, room.pc_flg, room.hall, room.floor, room.school_id"
			+ "from room where room_name Like '%' || :room_name || '%'")
	public List<Room> roomSelect(String room_name);
}
