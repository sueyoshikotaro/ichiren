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
	private String taskCategory;
	private String taskName;
	private String taskContent;
	private String taskStatus;
	private String startDate;
	private String endDate;
	private int taskPriority;
	private int taskLevel;
	private double taskWeight;
	private int progress;
	private boolean taskFlg;
	private String user_id;
	private int group_id;
}