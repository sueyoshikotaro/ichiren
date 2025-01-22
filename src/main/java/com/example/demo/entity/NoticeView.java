package com.example.demo.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeView {

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
