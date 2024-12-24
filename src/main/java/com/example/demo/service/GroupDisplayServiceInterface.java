package com.example.demo.service;

import com.example.demo.entity.Teams;

public interface GroupDisplayServiceInterface {

	// グループ一覧表示
	public Iterable<Teams> groupList();
	
}
