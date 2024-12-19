package com.example.demo.form;

import lombok.Data;

/*
 * リクエストパラメータをまとめて受け取るためのクラス
 */
@Data
public class TeForm {

	private String user_id;
	private String user_name;
	private String user_pass;
	private String user_rpass;
	
}
