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

}
