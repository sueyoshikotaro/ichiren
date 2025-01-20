package com.example.demo.form;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class GroupMemberDeleteView {

	@Id
	private String user_id;
	private String user_name;
	private String task_id;
	private String task_name;
	private String task_content;
	private int group_id;
	
}
