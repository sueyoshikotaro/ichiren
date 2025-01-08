package com.example.demo.service.impl;

import java.util.List;

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
	public List<Teams> groupList(String estYear) {

		return groupDispCrudRepo.groupList();
	}

//	/*
//	 * 管理者_グループ一覧
//	 * グループ発足年度で絞り込む
//	 */
//	@Override
//	public List<Teams> selectGroupByEstYear(String estYear) {
//
//		List<Teams> result;
//		if (estYear.equals(estYear)) {
//			result = groupDispCrudRepo.groupList();
//		} else {
//			result = groupDispCrudRepo.selectGroupByEstYear(estYear);
//		}
//		return result;
//
//		//		return groupDispCrudRepo.selectGroupByEstYear(estYear);
//	}


}
