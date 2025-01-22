package com.example.demo.form;

import lombok.Data;

@Data
public class TaskView {
	
	private int request_id;
	private String req_category;
	private String req_name;
	private String req_content;
	private String req_reason;
	private String add_date;
	private String user_name;
	private String start_date;
	private String end_date;
	private int task_priority;
	private int task_level;
	private int task_weight;
}
