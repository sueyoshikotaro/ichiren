package com.example.demo.form;

import lombok.Data;

/*
 * リクエストパラメータをまとめて受け取めるためのformクラス
 */
@Data
public class UserForm {
 
	private String user_id;
	private String user_name;
	private String user_pass;
	private String school_id;
	private String enr_year;
	private String user_flg;
 
}
