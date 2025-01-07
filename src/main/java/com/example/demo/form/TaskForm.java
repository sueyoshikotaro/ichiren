package com.example.demo.form;

import lombok.Data;

@Data
public class TaskForm {
	
	private int task_id;
	private String task_category;
	private String task_name;
	private String task_content;
	private String task_status;
	private String start_date;
	private String end_date;
	private String task_priority;
	private String task_level;
	private String task_weight;
	private String progress;
	private String task_flg;
	private String group_id;
	private String user_id;
	
	private String user_name;
}
