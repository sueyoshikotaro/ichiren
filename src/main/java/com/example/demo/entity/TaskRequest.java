package com.example.demo.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
	@Id
	private int request_id;
	private String req_category;
	private String req_name;
	private String req_content;
	private String req_reason;
	private Date app_date;
	private String user_id;
}
