package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.repository.GroupCrudRepository;
import com.example.demo.service.GroupServiceInterface;

public class GroupServiceImpl implements GroupServiceInterface {

	@Autowired
	GroupCrudRepository userCrudRepo;

	/**
	 * 坂本
	 * ユーザ
	 * 所属グループ一覧画面表示
	 */
//	@Override
//	public List<Teams> deptGroupList() {
//
//		return userCrudRepo.deptGroupList();
//	}
}
