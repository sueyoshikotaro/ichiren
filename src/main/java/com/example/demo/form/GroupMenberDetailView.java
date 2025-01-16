package com.example.demo.form;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class GroupMenberDetailView {

	@Id
	private String user_name;
	private String user_id;
	private int score;
	private int user_progress;
	
	private String task_name;
	private int task_priority;
	private int progress;
}
