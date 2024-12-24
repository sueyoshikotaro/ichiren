package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.Teams;
import com.example.demo.repository.GroupDisplayCrudRepository;
import com.example.demo.service.GroupDisplayServiceInterface;

public class GroupDisplayImpl implements GroupDisplayServiceInterface {

	@Autowired
	GroupDisplayCrudRepository groupDispCrudRepo;
	
	/*
	 * 向江
	 * グループ一覧
	 */
	@Override
	public Iterable<Teams> groupList() {
		
		return groupDispCrudRepo.groupList();
	}

}
