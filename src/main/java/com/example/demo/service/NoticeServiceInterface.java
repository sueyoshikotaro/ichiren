package com.example.demo.service;

import java.util.List;

import com.example.demo.form.NoticeViewForm;

public interface NoticeServiceInterface {

	// 連絡事項一覧表示
	public List<NoticeViewForm> noticeDisp(int group_id);
	
	// 連絡事項作成登録
	public void noticeRegist(String title, String contact_msg, int view_count, String send_by, int group_id);
	
	// 連絡事項削除
	public void noticeDelete(int notice_id);
	
	//チェックボックスで選択された連絡事項検索
	public List<NoticeViewForm> selectNotice(int notice_id);
}
