package com.example.demo.service;

import java.util.List;

import com.example.demo.form.GroupDisplay;

public interface GroupServiceInterface {

	/**
	 * 坂本
	 * ユーザ
	 * 所属グループ一覧
	 */
	public List<GroupDisplay> deptGroupList(String user_id);
	
	
}
