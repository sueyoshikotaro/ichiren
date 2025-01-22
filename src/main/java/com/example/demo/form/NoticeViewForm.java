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
	private String view_content;
	private String send_by;
	private int group_id;
	private String user_roll;
}
