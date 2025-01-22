package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.form.GroupDetailView;
import com.example.demo.form.GroupMemberDeleteView;
import com.example.demo.form.GroupMemberDetailView;
import com.example.demo.form.TaskForm;
import com.example.demo.form.TeamsForm;
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
	public List<TeamsForm> groupList(String dropdown, String dropid, int school_id) {
		
		
		
		List<TeamsForm> result = null;
		if (dropdown.equals("--")) {
			result = groupDispCrudRepo.groupList(school_id);
		} else {
			if (dropid.equals("year")) {
				if (dropdown.equals("グループ結成年度")) {
					result = groupDispCrudRepo.groupList(school_id);
				} else {
					result = groupDispCrudRepo.groupListYear(dropdown);
				}
			} else if (dropid.equals("school")) {
				if (dropdown.equals("学校名")) {
					result = groupDispCrudRepo.groupList(school_id);
				} else {
					result = groupDispCrudRepo.groupListSchool(dropdown);
				}
			} else if (dropid.equals("genre")) {
				if (dropdown.equals("ジャンル")) {
					result = groupDispCrudRepo.groupList(school_id);
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
	public List<TeamsForm> findAll() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	/*
	 * 湊原
	 * 管理者_グループ一覧
	 * グループ一覧
	 */
	@Override
	public List<TeamsForm> getTeamsByCriteria(String schoolName) {

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
	public List<GroupMemberDetailView> groupMemberDetail(String user_id, String group_id) {

		return groupDispCrudRepo.groupMemberDetail(user_id, group_id);
	}

	@Override
	public List<TeamsForm> findDistinctEstYear(String estYear) {
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

	/**
	 * 末吉
	 * メンバが受け持つ全てのタスクを取得
	 */
	@Override
	public List<TaskForm> taskList(String user_id, String group_id) {
		
		return groupDispCrudRepo.taskList(user_id, group_id);
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
	public List<GroupMemberDeleteView> grMemDelDisp(String user_id) {

		return groupDispCrudRepo.grMemDelDisp(user_id);
	}

	/*
	 * 向江
	 * グループメンバ削除確認
	 */
	@Override
	public void groupMemberDelete(String group_id, String user_id) {

		groupDispCrudRepo.groupMemberDelete(group_id, user_id);

	}

	/*
	 * 向江
	 * グループメンバ削除確認
	 * user_detailテーブルのscoreを昇順に並び変える
	 */
	@Override
	public List<GroupMemberDeleteView> membersScore(String group_id) {
		
		List<GroupMemberDeleteView> group = groupDispCrudRepo.membersScore(group_id);
		
		// membersScore の一つ目の要素を取得
		GroupMemberDeleteView firstMember = group.get(0);
		
		// score と user_id を含む新しいオブジェクトを返す
		List<GroupMemberDeleteView> result = new ArrayList<>();
		result.add(firstMember);
		
		return result;
		
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

	/**
	 * 末吉
	 * グループ作成
	 */
	@Override
	public void groupDetailCreate(String user_id, int group_id, String user_roll, int score) {

		groupDispCrudRepo.groupDetailCreate(user_id, group_id, user_roll, score);
	}
  
	/**
	 * 末吉
	 * 既に登録されているユーザIDを取得
	 */
	@Override
	public List<String> getExistUserIds(Integer group_id) {
		
		return groupDispCrudRepo.getExistUserIds(group_id);
	}

	/**
	 * 末吉
	 * グループ解散完了
	 */
	@Override
	public void groupDiss(int group_id) {
		
		groupDispCrudRepo.groupDiss(group_id);
		
	}

	/**
	 * 末吉
	 * グループ編集完了
	 */
	@Override
	public void groupEdit(String user_id, int group_id, String user_roll) {
		
		groupDispCrudRepo.groupEdit(user_id, group_id, user_roll);
		
	}

	/**
	 * 末吉
	 * タスクの再割り振り
	 */
	@Override
	public void updateUserId(int task_id, String user_id) {
		
		groupDispCrudRepo.updateUserId(task_id, user_id);
		
	}

	
	@Override
	public void updateScore(String user_id, String group_id, int scoreResult, int userProgressResult) {
		
		groupDispCrudRepo.updateScore(user_id, group_id, scoreResult, userProgressResult);
		
	}


	

	
}
