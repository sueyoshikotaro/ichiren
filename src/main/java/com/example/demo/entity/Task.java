package com.example.demo.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
	@Id
	private int task_id;
	private String task_category;
	private String task_name;
	private String task_content;
	private String task_status;
	private Date start_date;
	private Date end_date;
	private int task_priority;
	private int task_level;
	private int task_weight;
	private int progress;
	private boolean task_flg;
	private String user_id;
	private int group_id;
	
	private String user_name;
}