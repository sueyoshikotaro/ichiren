package com.example.demo.form;

import lombok.Data;

@Data
public class TaskReqForm {
	
	private int request_id;
	private String req_category;
	private String req_name;
	private String req_content;
	private String req_reason;
	private String add_date;
	private String user_name;
	private int request_flg;
	private String user_id;
}
