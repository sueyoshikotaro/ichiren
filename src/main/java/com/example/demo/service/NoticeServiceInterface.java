package com.example.demo.service;

import java.util.List;

import com.example.demo.form.NoticeViewForm;

public interface NoticeServiceInterface {

	// 連絡事項一覧表示
	public List<NoticeViewForm> noticeDisp(int group_id);
}
