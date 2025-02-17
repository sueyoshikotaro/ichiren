package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.form.GroupDisplay;
import com.example.demo.form.GroupMemberDetailView;
import com.example.demo.form.SchoolDisplay;
import com.example.demo.form.TaskForm;
import com.example.demo.form.TeamsDisplay;
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
	public List<TeamsDisplay> groupList(String year, String genre, int school_id) {

		List<TeamsDisplay> result = null;
		if (genre == null) {
			result = groupDispCrudRepo.groupList1(year, school_id);
		} else {
			if (genre.equals("選択なし") && year.equals("選択なし") && school_id == 0) {
				result = groupDispCrudRepo.groupList();
			} else {
				if (!genre.equals("選択なし") && !year.equals("選択なし")) {
					result = groupDispCrudRepo.groupList(year, genre);
				}
				if (!genre.equals("選択なし") && school_id != 0) {
					result = groupDispCrudRepo.groupList2(genre, school_id);
				}
				if (!year.equals("選択なし") && school_id != 0) {
					result = groupDispCrudRepo.groupList1(year, school_id);
				}

				if (!genre.equals("選択なし") && year.equals("選択なし") && school_id == 0) {
					result = groupDispCrudRepo.groupListGenre(genre);
				}
				if (genre.equals("選択なし") && !year.equals("選択なし") && school_id == 0) {
					result = groupDispCrudRepo.groupListYear(year);
				}
				if (genre.equals("選択なし") && year.equals("選択なし") && school_id != 0) {
					result = groupDispCrudRepo.groupListSchool(school_id);
				}

				if (!genre.equals("選択なし") && !year.equals("選択なし") && school_id != 0) {
					result = groupDispCrudRepo.groupList(genre, year, school_id);
				}
			}
		}
		return result;
	}

	/**
	 * 末吉
	 * 絞り込み検索結果
	 */
	@Override
	public List<GroupDisplay> groupInfo(int group_id) {

		return groupDispCrudRepo.groupInfo(group_id);
	}

	/*
	 * 湊原
	 * 管理者_グループ一覧
	 * グループ一覧
	 */
	@Override
	public List<TeamsDisplay> getTeamsByCriteria(String schoolName) {

		return groupDispCrudRepo.findByCriteria(schoolName);
	}

	/*
	 * 向江
	 * グループ詳細
	 */
	@Override
	public List<GroupDisplay> groupDetail(int group_id) {

		return groupDispCrudRepo.groupDetail(group_id);
	}

	/*
	 * 向江
	 * グループメンバ詳細
	 */
	@Override
	public List<GroupMemberDetailView> groupMemberDetail(String user_id, int group_id) {

		return groupDispCrudRepo.groupMemberDetail(user_id, group_id);
	}

	/*
	 * 向江
	 * タスク詳細
	 */
	@Override
	public List<TaskForm> taskDetail(int task_id) {

		return groupDispCrudRepo.taskDetail(task_id);
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
	public List<GroupMemberDetailView> grMemDelDisp(String user_id, int group_id) {

		return groupDispCrudRepo.grMemDelDisp(user_id, group_id);
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
	public List<GroupMemberDetailView> membersScore(int group_id) {

		List<GroupMemberDetailView> group = groupDispCrudRepo.membersScore(group_id);

		return group;

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
		//解散
		groupDispCrudRepo.groupDiss(group_id);

		//タスクフラグをfalseに
		groupDispCrudRepo.taskDiss(group_id);
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
		List<GroupMemberDetailView> group = groupDispCrudRepo.membersScore(group_id);

		//削除するユーザを割り振る候補から除外する
		group.removeIf(member -> member.getUser_id().equals(user_id));

		//タスクを割り振るメンバのscoreを取得
		int score = group.get(0).getScore();

		//割り振るユーザのタスク情報を取得
		List<TaskForm> deleteUserTask = groupDispCrudRepo.taskList(user_id, group_id);

		//割り振られるユーザのタスク情報を取得
		List<TaskForm> updateUserTask = groupDispCrudRepo.taskList(group.get(0).getUser_id(), group_id);
		
		System.out.println("割り振られる！！！");
		System.out.println(updateUserTask);
		System.out.println("タスクのサイズ:" + updateUserTask.size());
		
		//割り振るタスクのtask_weightを取得
		task_weight = deleteUserTask.get(0).getTask_weight();

		//割り振ったメンバのscoreを再計算(戻り値)
		scoreResult = score + task_weight;

		//進捗度を足す_タスクがまだ割り振られていない場合
		if(updateUserTask.isEmpty()) {
			progressSum = 0 + deleteUserTask.get(0).getProgress();
			
			//タスクがすでに割り振られている場合
		} else {
			System.out.println("score計算:" + updateUserTask.get(0).getUser_progress() + "*" + updateUserTask.size() + "+" + deleteUserTask.get(0).getProgress());
			progressSum = updateUserTask.get(0).getUser_progress() * updateUserTask.size() + deleteUserTask.get(0).getProgress();
		}

		//変更後の進捗度を計算(戻り値)
		System.out.println("score計算:" + progressSum + "/" + updateUserTask.size() + 1);
		userProgressResult = progressSum / (updateUserTask.size() + 1);
		System.out.println("こたえ:" + userProgressResult);
		

		//戻り値をresult配列に格納
		Object[] result = { scoreResult, userProgressResult, group.get(0).getUser_id() };

		return result;
	}

	/**
	 * 末吉
	 * タスク進捗を変更したユーザの進捗更新
	 */
	@Override
	public void updateProgress(String user_id, int group_id) {

		//タスクの進捗度の合計値格納
		int progressSum = 0;

		//割り振られるユーザのタスク情報を取得
		List<TaskForm> updateUserTask = groupDispCrudRepo.taskList(user_id, group_id);

		//ユーザの進捗度の計算
		for (TaskForm i : updateUserTask) {
			progressSum += i.getProgress();
		}

		//更新するユーザ
		int userProgressResult = progressSum / updateUserTask.size();

		groupDispCrudRepo.updateProgress(group_id, user_id, userProgressResult);

	}

	//グループの全体進捗更新
	@Override
	public void allProgress(int group_id) {

		//メンバごとの進捗度の合計を格納
		int all_progress = 0;

		//更新後のグループ全体の進捗度を格納
		int all_progress_result = 0;

		int taskCnt = 0;
		
		List<GroupDisplay> taskList = groupDispCrudRepo.groupDetail(group_id);

		//全体進捗の計算
		for (GroupDisplay progressSum : taskList) {
			all_progress += progressSum.getUser_progress();
			
			if(progressSum.getUser_progress() != 0) {
				taskCnt++;
			}
		}
		
		//グループにメンバがいる場合
		if (!taskList.isEmpty()) {
			//更新後の全体進捗の計算
			all_progress_result = all_progress / taskCnt;
		} else {
			all_progress_result = 0;
		}

		//全体進捗の更新
		groupDispCrudRepo.allProgressUpdate(group_id, all_progress_result);
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
	public List<GroupDisplay> setChatUser(int school_id, String user_roll) {

		//チャット相手を一覧で格納
		return groupDispCrudRepo.setChatUser(school_id, user_roll);
	}

	//チャット相手を検索
	@Override
	public List<GroupDisplay> chatPartnerSearch(int school_id, String search, String user_roll) {
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
	public List<GroupMemberDetailView> memberDetail(String user_id, int group_id, String selectedValue) {

		List<GroupMemberDetailView> result = null;
		if (selectedValue.equals("選択なし")) {
			result = groupDispCrudRepo.memberDetail(user_id, group_id);
		} else {
			result = groupDispCrudRepo.memberDetail(user_id, group_id, selectedValue);
		}

		return result;
	}

	/**
	 * 湊原
	 * 学校一覧(絞り込み用)
	 */
	@Override
	public List<TeamsDisplay> selectSchool() {
		return groupDispCrudRepo.selectSchool();
	}

	/**
	 * 湊原
	 * グループ結成年度一覧(絞り込み用)
	 */
	@Override
	public List<TeamsDisplay> selectEstYear(String value) {
		List<TeamsDisplay> result = null;
		if (value.equals("user")) {
			result = groupDispCrudRepo.selectuserEstYear();
		} else if (value.equals("group")) {
			result = groupDispCrudRepo.selectgroupEstYear();
		}
		return result;

	}

	/**
	 * 湊原
	 * ジャンル一覧(絞り込み用)
	 */
	@Override
	public List<TeamsDisplay> selectGenre() {
		return groupDispCrudRepo.selectGenre();
	}

	/**
	 * 末吉
	 * 居場所一覧
	 */
	@Override
	public List<SchoolDisplay> roomSelect(int school_id) {

		return groupDispCrudRepo.roomSelect(school_id);
	}

	/**
	 * 末吉
	 * 居場所更新
	 */
	@Override
	public void roomUpdate(String work_status, int group_id) {

		groupDispCrudRepo.roomUpdate(work_status, group_id);
	}

}
