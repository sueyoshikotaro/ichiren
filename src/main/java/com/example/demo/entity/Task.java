package com.example.demo.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
	@Id
	private Integer task_id;
	private String task_category;
	private String task_name;
	private String task_content;
	private String task_status;
	private String start_date;
	private String end_date;
	private int task_priority;
	private int task_level;
	private double task_weight;
	private int progress;
	private boolean task_flg;
	private String user_id;
	private int group_id;
}