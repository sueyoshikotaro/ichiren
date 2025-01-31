package com.example.demo.service;

import java.util.List;

import com.example.demo.form.GroupDetailView;
import com.example.demo.form.GroupDisplay;
import com.example.demo.form.GroupMemberDeleteView;
import com.example.demo.form.GroupMemberDetailView;
import com.example.demo.form.Room;
import com.example.demo.form.TaskForm;
import com.example.demo.form.TeamsForm;

public interface GroupDisplayServiceInterface {

	// グループ一覧表示
	public List<TeamsForm> groupList(String est_year, String genre, int school_id);

	//湊原
	public List<TeamsForm> getTeamsByCriteria(String schoolName);

	//グループ詳細表示
	public List<GroupDetailView> groupDetail(int group_id);

	//グループメンバ詳細表示
	public List<GroupMemberDetailView> groupMemberDetail(String user_id, String group_id);

	//グループメンバタスク詳細表示
	public List<TaskForm> taskDetail(String task_name);

	//メンバが受け持つ全てのタスクを取得
	public List<TaskForm> taskList(String user_id, int group_id);

	//グループ編集
	public void groupEdit(String group_id);

	//グループメンバ削除画面を表示するためだけのメソッド
	public List<GroupMemberDeleteView> grMemDelDisp(String user_id);

	//グループメンバ削除
	public void groupMemberDelete(int group_id, String user_id);

	//グループメンバ削除user_detailテーブルのscoreを昇順に並び変える
	public List<GroupMemberDeleteView> membersScore(int group_id);

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

	//タスクの再割り振り
	public void updateUserId(int task_id, String user_id);

	//user_detailのscoreとuser_progressを更新する
	public void updateScore(String user_id, int group_id, int scoreResult, int userProgressResult);

	//グループメンバ削除のリファクタリング(試しに)
	public Object[] scoreCalc(int group_id, String user_id);

	//タスクの進捗を更新したメンバの進捗更新
	public void updateProgress(String user_id, int group_id);

	//グループの全体進捗更新
	public void allProgress(int group_id);

	//所属グループ一覧
	public List<GroupDisplay> deptGroupList(String user_id);

	//チャット相手を設定
	public List<GroupDetailView> setChatUser(int school_id, String user_roll);

	//チャット相手検索
	public List<GroupDetailView> chatPartnerSearch(int school_id, String search, String user_roll);

	//メンバ一覧表示
	public List<GroupMemberDetailView> memberList(int group_id);

	//グループの進捗取得
	public int selectProgress(int attribute);

	//メンバ詳細取得
	public List<GroupMemberDetailView> memberDetail(String user_id, String group_id, String seletedValue);

	//結成年度取得(絞り込み用)
	public List<TeamsForm> selectEstYear(String value);

	//ジャンル取得(絞り込み用)
	public List<TeamsForm> selectGenre();

	//学校一覧(絞り込み用)
	List<TeamsForm> selectSchool();

	//居場所選択
	public List<Room> roomSelect(int school_id);

	//居場所更新
	public void roomUpdate(String work_status, int group_id);

}
