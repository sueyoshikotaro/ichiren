package com.example.demo.form;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class NoticeViewForm {

	@Id
	private String user_name;
	private String user_id;
	private String notice_id;
	private String title;
	private String contact_msg;
	private String send_date;
	private int view_count;
	private String send_by;
	private int group_id;
	
	// 連絡事項選択のチェック欄
	private boolean checked;
//	private String user_roll;
}
