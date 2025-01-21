package com.example.demo.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberDetailView {

	@Id
	private String user_id;
	private String user_name;
	private int score;
	private int user_progress;
	
	private String task_name;
	private int task_priority;
	private int progress;
	private int group_id;
	
	
}
