package com.example.demo.service;

import java.util.List;

import com.example.demo.form.Room;

public interface RoomServiceInterface {

	//教室一覧
	public List<Room> roomCheck(String room_name);
}
