package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.form.GroupDisplay;
import com.example.demo.repository.GroupCrudRepository;
import com.example.demo.service.GroupServiceInterface;

public class GroupServiceImpl implements GroupServiceInterface {

	@Autowired
	GroupCrudRepository groupCrudRepo;

	/**
	 * 坂本
	 * ユーザ
	 * 所属グループ一覧画面表示
	 */
	@Override
	public List<GroupDisplay> deptGroupList(String user_id) {

		return groupCrudRepo.deptGroupList(user_id);
	}

	/**
	 * 坂本
	 * リーダ・メンバ
	 * メニュー
	 */
	@Override
	public List<GroupDisplay> selectRoll() {

		return groupCrudRepo.selectRoll();
	}
}
