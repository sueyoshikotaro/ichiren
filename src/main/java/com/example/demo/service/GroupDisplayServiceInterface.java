package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Teams;
import com.example.demo.form.GroupDetailView;
import com.example.demo.form.GroupMenberDetailView;

public interface GroupDisplayServiceInterface {

	// グループ一覧表示
	public List<Teams> groupList(String dropdown,String dropid);

	// 発足年度で絞り込みする
	public List<Teams> findDistinctEstYear(String estYear);

	public List<Teams> findAll();

	// cahtGPT
	public List<Teams> getTeamsByCriteria(String estYear, String schoolName, String genre);

	//湊原
	public List<Teams> getTeamsByCriteria(String schoolName);
	
	//グループ詳細表示
	public List<GroupDetailView> groupDetail(String group_id);
	
	//グループメンバ詳細表示
	public List<GroupMenberDetailView> groupMemberDetail(String user_id);
}
