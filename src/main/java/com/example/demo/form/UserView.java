package com.example.demo.form;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class UserView {
	
	@Id
	private String user_id;
	private String user_name;
	private String user_pass;
	private String school_name;
	private String enr_year;
	private int user_flg;
	

}
