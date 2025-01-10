package com.example.demo.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDetailView {

	@Id
	private String group_id;
	private String group_name;
	private String genre;
	private String all_progress;
	
	private String user_name;
	private int user_progress;
	private int score;
	
}
