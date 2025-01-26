package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.form.GroupDetailView;
import com.example.demo.form.GroupDisplay;
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
	public List<GroupDetailView> groupDetail(int group_id) {

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
	public List<TaskForm> taskList(String user_id, int group_id) {

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
	public void groupMemberDelete(int group_id, String user_id) {

		groupDispCrudRepo.groupMemberDelete(group_id, user_id);

	}

	/*
	 * 向江
	 * グループメンバ削除確認
	 * user_detailテーブルのscoreを昇順に並び変える
	 */
	@Override
	public List<GroupMemberDeleteView> membersScore(int group_id) {

		List<GroupMemberDeleteView> group = groupDispCrudRepo.membersScore(group_id);

		return group;
		//		// membersScore の一つ目の要素を取得
		//		GroupMemberDeleteView firstMember = group.get(0);
		//		
		//		// score と user_id を含む新しいオブジェクトを返す
		//		List<GroupMemberDeleteView> result = new ArrayList<>();
		//		result.add(firstMember);

		//		return result;

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
	public void updateScore(String user_id, int group_id, int scoreResult, int userProgressResult) {

		groupDispCrudRepo.updateScore(user_id, group_id, scoreResult, userProgressResult);

	}

	//更新後のscoreとprogressの値の計算
	@Override
	public Object[] scoreCalc(int group_id, String user_id) {

		//メンバごとの進捗度の合計を格納
		int progressSum = 0;

		//割り振るタスクのtask_weightを取得
		int task_weight = 0;

		//割り振ったメンバのscoreを再計算
		int scoreResult = 0;

		//メンバの変更後の進捗度の計算結果を格納
		int userProgressResult = 0;

		//user_detailの情報をscoreの昇順に格納
		List<GroupMemberDeleteView> group = groupDispCrudRepo.membersScore(group_id);

		//タスクを割り振るメンバのscoreを取得
		int score = group.get(0).getScore();

		//割り振るユーザのタスク情報を取得
		List<TaskForm> delteUserTask = groupDispCrudRepo.taskList(user_id, group_id);

		//割り振られるユーザのタスク情報を取得
		List<TaskForm> updateUserTask = groupDispCrudRepo.taskList(group.get(0).getUser_id(), group_id);

		//割り振るタスクのtask_weightを取得
		task_weight = delteUserTask.get(0).getTask_weight();

		//割り振ったメンバのscoreを再計算(戻り値)
		scoreResult = score + task_weight;

		progressSum = updateUserTask.get(0).getUser_progress() + delteUserTask.get(0).getProgress();

		//変更後の進捗度を計算(戻り値)
		userProgressResult = progressSum / 2;

		//戻り値をresult配列に格納
		Object[] result = { scoreResult, userProgressResult, group.get(0).getUser_id() };

		return result;
	}

	//グループの全体進捗更新
	@Override
	public void allProgress(int group_id) {

		//メンバごとの進捗度の合計を格納
		int all_progress = 0;

		//更新後のグループ全体の進捗度を格納
		int all_progress_result = 0;

		List<GroupDetailView> taskList = groupDispCrudRepo.groupDetail(group_id);

		System.out.println(taskList.size());

		for (GroupDetailView progressSum : taskList) {
			all_progress += progressSum.getUser_progress();
		}

		//更新後の全体進捗の計算
		all_progress_result = all_progress / taskList.size();

		//全体進捗の更新
		groupDispCrudRepo.allProgressUpdate(taskList.get(0).getGroup_id(), all_progress_result);

	}

	/**
	 * 坂本
	 * 所属グループ一覧の表示
	 */
	@Override
	public List<GroupDisplay> deptGroupList(String user_id) {

		return groupDispCrudRepo.deptGroupList(user_id);
	}

	//チャット相手を設定
	@Override
	public List<GroupDetailView> setChatUser(int school_id, String user_roll) {

		//チャット相手を一覧で格納
		return groupDispCrudRepo.setChatUser(school_id, user_roll);
	}

	//チャット相手を検索
	@Override
	public List<GroupDetailView> chatPartnerSearch(int school_id, String search, String user_roll) {
		return groupDispCrudRepo.chatPartnerSearch(school_id, search, user_roll);
	}

	/**
	 * 湊原
	 * ユーザ側のメンバー一覧
	 */
	@Override
	public List<GroupMemberDetailView> memberList(int group_id) {
		return groupDispCrudRepo.memberList(group_id);

	}

	/**
	 * 湊原
	 * ユーザ側のグループの全体進捗
	 */
	@Override
	public int selectProgress(int group_id) {
		return groupDispCrudRepo.selectAllprogress(group_id);
	}

	@Override
	public List<GroupMemberDetailView> memberDetail(String user_id, String group_id, String selectedValue) {

		List<GroupMemberDetailView> result = null;
		if (selectedValue.equals("--")) {
			result = groupDispCrudRepo.memberDetail(user_id, group_id);
		}else {
			result = groupDispCrudRepo.memberDetail(user_id, group_id, selectedValue);
		}
		
		return result;
	}

}
