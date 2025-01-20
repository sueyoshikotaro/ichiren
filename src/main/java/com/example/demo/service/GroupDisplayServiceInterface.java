package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Teams;
import com.example.demo.form.GroupDetailView;
import com.example.demo.form.GroupMemberDetailView;
import com.example.demo.form.TaskForm;

public interface GroupDisplayServiceInterface {

	// グループ一覧表示
	public List<Teams> groupList(String dropdown,String dropid);

	// 発足年度で絞り込みする
	public List<Teams> findDistinctEstYear(String estYear);

	public List<Teams> findAll();

	//湊原
	public List<Teams> getTeamsByCriteria(String schoolName);
	
	//グループ詳細表示
	public List<GroupDetailView> groupDetail(String group_id);
	
	//グループメンバ詳細表示
	public List<GroupMemberDetailView> groupMemberDetail(String user_name);
	
	//グループメンバタスク詳細表示
	public List<TaskForm> taskDetail(String task_name);
	
	//グループ編集
	public void groupEdit(String group_id);

	//グループメンバ削除画面を表示するためだけのメソッド
	public List<GroupMemberDetailView> grMemDelDisp(String user_name);
	
	//グループメンバ削除
	public void groupMemberDelete(String group_id, String user_id);
	
	//グループ作成
	public void groupCreate(String group_name, int school_id, String genre);
	
	//登録したグループID取得
	public int MaxGroupId(int school_id);
	
	//グループ詳細登録
	public void groupDetailCreate(String user_id, int group_id, String user_roll, int score);

	//既に登録されているユーザIDを取得
	public List<String> getExistUserIds(Integer group_id);
	
}
