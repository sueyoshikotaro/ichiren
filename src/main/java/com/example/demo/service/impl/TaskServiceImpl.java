package com.example.demo.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.Task;
import com.example.demo.entity.TaskReqView;
import com.example.demo.repository.TaskCrudRepository;
import com.example.demo.repository.TaskReqCrudRepository;
import com.example.demo.service.TaskServiceInterface;

public class TaskServiceImpl implements TaskServiceInterface {
	//湊原
	@Autowired
	TaskCrudRepository repo;

	@Autowired
	TaskReqCrudRepository repo_req;

	/**
	 * 湊原
	 * タスク一覧表示
	 */
	@Override
	public List<Task> taskDisplayList(String user, int group_id) {
		List<Task> result;
		if (user.equals("全員")) {
			result = repo.selectTask(group_id);

		} else {
			result = repo.selectTask(user, group_id);
		}
		return result;
	}

	/**
	 * 湊原
	 * タスク登録
	 */
	@Override
	public void taskRegister(String task_category, String task_name, String task_content, String str,
			Date start_date, Date end_date, String task_priority, String task_level, String task_weight,
			String user_name, int group_id) {

		repo.registerTask(task_category, task_name, task_content, str,
				start_date, end_date, task_priority, task_level, task_weight, user_name, group_id);

	}

	/**
	 * タスク担当者検索
	 * 湊原
	 */
	@Override
	public Iterable<String> taskUserSearch(int group_id) {
		return repo.selectTaskByUser(group_id);
	}

	/**
	 * 湊原
	 * タスク編集
	 */
	@Override
	public void taskUpdate(int task_id, String task_category, String task_name, String task_content,
			String task_priority, String task_weight, String user_name) {

		repo.updateTask(task_id, task_category, task_name, task_content,
				task_priority, task_weight, user_name);
	}

	/**
	 * タスク削除(フラグ更新のみ)
	 */
	@Override
	public void taskUpFlg(int task_id) {
		repo.updateFlg(task_id);
	}

	/**
	 * スコアを取得するメソッド
	 * 湊原
	 */
	@Override
	public int userScore(String user_name, int group_id) {
		return repo.selectScore(user_name, group_id);
	}

	/**
	 * 湊原
	 * タスクの編集や削除、登録に伴うスコアの更新
	 */
	@Override
	public void userUpScore(int score, String user_name, int group_id) {
		repo.updateScore(score, user_name, group_id);
	}

	/*
	 * 向江
	 * 未承認タスク一覧
	 */
	@Override
	public List<TaskReqView> selectTaskUnapproved() {

		return repo_req.selectTaskUnapproved();
	}

	/*
	 * 未承認確認表示
	 * 湊原
	 * 未承認テーブルフラグ更新
	 */
	@Override
	public void taskReqFlg() {

	}

}
