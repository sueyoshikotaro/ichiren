package com.example.demo.service;

import java.util.Date;
import java.util.List;

import com.example.demo.entity.Task;
import com.example.demo.entity.TaskReqView;

public interface TaskServiceInterface {
	//タスク一覧表示
	public List<Task> taskDisplayList(String user,int group_id);
	
	//タスク登録
	public void taskRegister(String task_category, String task_name, String task_content, String string,
			Date start_date, Date end_date, String task_priority, String task_level, String task_weight, String user_name, String group_id);
		
	//タスク担当者検索
	public Iterable<String> taskUserSearch(int group_id);
	
	//タスク編集
	public void taskUpdate(int task_id, String task_category, String task_name, String task_content,
			String task_priority, String task_weight, String user_name);
	
	//タスクフラグ更新(削除)
	public void taskUpFlg(int task_id);
	
	//スコアを取得するメソッド
	public int userScore(String user_id, String group_id);
	
	//タスク未承認一覧表示
	public List<TaskReqView> selectTaskUnapproved();
	
	//タスク未承認確認表示
	public List<TaskReqView> taskUnapprovedConfirm();
}
