package com.example.demo.service;

import java.util.List;

import com.example.demo.form.GroupDetailView;
import com.example.demo.form.GroupMemberDeleteView;
import com.example.demo.form.GroupMemberDetailView;
import com.example.demo.form.TaskForm;
import com.example.demo.form.TeamsForm;

public interface GroupDisplayServiceInterface {

	// グループ一覧表示
	public List<TeamsForm> groupList(String dropdown, String dropid, int school_id);

	// 発足年度で絞り込みする
	public List<TeamsForm> findDistinctEstYear(String estYear);

	public List<TeamsForm> findAll();

	//湊原
	public List<TeamsForm> getTeamsByCriteria(String schoolName);

	//グループ詳細表示
	public List<GroupDetailView> groupDetail(String group_id);

	//グループメンバ詳細表示
	public List<GroupMemberDetailView> groupMemberDetail(String user_id, String group_id);

	//グループメンバタスク詳細表示
	public List<TaskForm> taskDetail(String task_name);

	//グループ編集
	public void groupEdit(String group_id);

	//グループメンバ削除画面を表示するためだけのメソッド
	public List<GroupMemberDeleteView> grMemDelDisp(String user_id);

	//グループメンバ削除
	public void groupMemberDelete(String group_id, String user_id, String user_name);
	
	//グループメンバ削除Taskテーブルのuser_idを編集する
	public void groupMemberDelete2(String user_id);
	
	//グループメンバ削除user_detailテーブルのscoreを昇順に並び変える
	public void groupMemberDelete3(String user_id);

	//グループ作成
	public void groupCreate(String group_name, int school_id, String genre);

	//登録したグループID取得
	public int MaxGroupId(int school_id);

	//グループ詳細登録
	public void groupDetailCreate(String user_id, int group_id, String user_roll, int score);

	//既に登録されているユーザIDを取得
	public List<String> getExistUserIds(Integer group_id);
	
	//グループ解散
	public void groupDiss(int group_id);
	
	//グループ編集
	public void groupEdit(String user_id, int group_id, String user_roll);
	
}
