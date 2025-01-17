package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.Teams;
import com.example.demo.form.GroupDetailView;
import com.example.demo.form.GroupMemberDetailView;
import com.example.demo.form.TaskForm;
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
	public List<Teams> groupList(String dropdown, String dropid) {
		List<Teams> result = null;
		if (dropdown.equals("--")) {
			result = groupDispCrudRepo.groupList();
		} else {
			if (dropid.equals("year")) {
				if (dropdown.equals("グループ結成年度")) {
					result = groupDispCrudRepo.groupList();
				} else {
					result = groupDispCrudRepo.groupListYear(dropdown);
				}
			} else if (dropid.equals("school")) {
				if (dropdown.equals("学校名")) {
					result = groupDispCrudRepo.groupList();
				} else {
					result = groupDispCrudRepo.groupListSchool(dropdown);
				}
			} else if (dropid.equals("genre")) {
				if (dropdown.equals("ジャンル")) {
					result = groupDispCrudRepo.groupList();
				} else {
					result = groupDispCrudRepo.groupListGenre(dropdown);
				}
			}
		}
		return result;
	}

	/*
	 * Codeium
	 * 向江
	 * 年だけ抽出
	 */
	@Override
	public List<Teams> findAll() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	/*
	 * 湊原
	 * 管理者_グループ一覧
	 * グループ一覧
	 */
	@Override
	public List<Teams> getTeamsByCriteria(String schoolName) {

		return groupDispCrudRepo.findByCriteria(schoolName);
	}

	/*
	 * 向江
	 * グループ詳細
	 */
	@Override
	public List<GroupDetailView> groupDetail(String group_id) {

		return groupDispCrudRepo.groupDetail(group_id);
	}

	/*
	 * 向江
	 * グループメンバ詳細
	 */
	@Override
	public List<GroupMemberDetailView> groupMemberDetail(String user_name) {

		return groupDispCrudRepo.groupMemberDetail(user_name);
	}

	@Override
	public List<Teams> findDistinctEstYear(String estYear) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	/*
	 * 向江
	 * タスク詳細
	 */
	@Override
	public List<TaskForm> taskDetail(String task_name) {

		return groupDispCrudRepo.taskDetail(task_name);
	}

	/*
	 * 向江
	 * グループ編集
	 */
	@Override
	public void groupEdit(String group_id) {

		groupDispCrudRepo.groupEdit(group_id);

	}

	/*
	 * 向江
	 * グループメンバ削除画面を表示するためだけのメソッド
	 */
	@Override
	public List<GroupMemberDetailView> grMemDelDisp(String user_name) {
		
		return groupDispCrudRepo.grMemDelDisp(user_name);
	}

	/*
	 * 向江
	 * グループメンバ削除
	 */
	@Override
	public void groupMemberDelete(String group_id, String user_id) {

		groupDispCrudRepo.groupMemberDelete(group_id, user_id);

	}

	/**
	 * 末吉
	 * グループ作成
	 */
	@Override
	public void groupCreate(String group_name, int school_id, String genre) {

		groupDispCrudRepo.groupCreate(group_name, school_id, genre);

	}

	/**
	 * 末吉
	 * 登録したグループID取得
	 */
	@Override
	public int MaxGroupId(int school_id) {

		return groupDispCrudRepo.MaxGroupId(school_id);
	}

	@Override
	public void groupDetailCreate(String user_id, int group_id, String user_roll, int score) {

		groupDispCrudRepo.groupDetailCreate(user_id, group_id, user_roll, score);
	}

}
