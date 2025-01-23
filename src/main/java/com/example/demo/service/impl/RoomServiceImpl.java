package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.form.Room;
import com.example.demo.repository.RoomCrudRepository;
import com.example.demo.service.RoomServiceInterface;

public class RoomServiceImpl implements RoomServiceInterface {

	@Autowired
	RoomCrudRepository roomCrudRepo;

	/*
	 * 坂本
	 * 居場所更新
	 */
	@Override
	public List<Room> roomCheck(String room_name) {

		return roomCrudRepo.roomSelect(room_name);
	}

}
