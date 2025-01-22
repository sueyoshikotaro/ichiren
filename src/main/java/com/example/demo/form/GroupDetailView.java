package com.example.demo.form;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class GroupDetailView {

	@Id
	private int group_id;
	private String group_name;
	private String genre;
	private String all_progress;
	
	private int school_id;
	private String user_id;
	private String user_name;
	private String user_roll;
	private int user_progress;
	private int score;
	
}
