package com.example.demo.service;

import java.util.Date;
import java.util.List;

import com.example.demo.entity.Task;
import com.example.demo.entity.TaskReqView;

public interface TaskServiceInterface {
	//タスク一覧表示
	public List<Task> taskDisplayList(String user, int group_id);

	//タスクの詳細データを取得
	public List<Task> taskDetails(int task_id, int group_id);

	//タスク登録
	public boolean taskRegister(String task_category, String task_name, String task_content, String string,
			Date start_date, Date end_date, int task_priority, int task_level, int task_weight,
			String user_name, int group_id);

	//タスク担当者検索
	public Iterable<String> taskUserSearch(int group_id);

	//タスク編集
	public void taskUpdate(int task_id, String task_category, String task_name, String task_content,
			int task_priority, int task_weight, String user_name);

	//タスクフラグ更新(削除)
	public void taskUpFlg(int task_id);

	//スコアを取得するメソッド
	public int userScore(String user_name, int group_id);

	//スコアを更新するメソッド
	public void userUpScore(int score, String user_name, int group_id);

	//タスクの進捗を更新するメソッド
	public void taskUpProgress(int task_id, int progress);

	//タスク未承認一覧表示
	public List<TaskReqView> selectTaskUnapproved(int group_id);

	//タスク未承認フラグ更新(承認済)
	public boolean taskReqFlg(int request_id);

	//タスク申請登録
	public void registerTaskReq(String req_category, String req_name, String req_content, String req_reason,
			Date add_date, String user_name, int group_id);

	//カテゴリ取得(絞り込み用)
	public List<Task> selectCategory(int group_id);

}
