/*
 * 向江
 * タスクリクエストのview
 */

package com.example.demo.entity;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class TaskReqView {

	@Id
	private int request_id;
	private String req_category;
	private String user_name;
	private String req_name;
	private String req_content;
	private String req_reason;
	private String add_date;
	private int request_flg;
//	private String user_id;
}
