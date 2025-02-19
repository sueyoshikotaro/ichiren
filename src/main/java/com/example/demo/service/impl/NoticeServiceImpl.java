package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.form.NoticeViewForm;
import com.example.demo.repository.NoticeCrudRepository;
import com.example.demo.service.NoticeServiceInterface;

public class NoticeServiceImpl implements NoticeServiceInterface {

	@Autowired
	NoticeCrudRepository noticeCrudRepo;
	
	/*
	 * 向江
	 * 連絡事項一覧
	 */
	@Override
	public List<NoticeViewForm> noticeDisp(int group_id) {
		
		return noticeCrudRepo.noticeDisp(group_id);
	}

	/*
	 * 向江
	 * r連絡事項作成登録
	 */
	@Override
	public void noticeRegist(String title, String contact_msg, int view_count, String send_by, int group_id) {
		
		noticeCrudRepo.noticeRegist(title, contact_msg, view_count, send_by, group_id);
		
	}

	@Override
	public void noticeDelete(int notice_id) {
		
		noticeCrudRepo.noticeDelete(notice_id);
		
	}

	@Override
	public List<NoticeViewForm> selectNotice(int notice_id) {
		return noticeCrudRepo.selectNotice(notice_id);
	}

}
