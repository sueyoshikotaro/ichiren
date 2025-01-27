package com.example.demo.form;

import java.util.Date;

import lombok.Data;

@Data
public class ChatForm {
	
	//chatroomテーブル
	private int room_id;
	private String user_id1;
	private String user_id2;
	
	
	//messageテーブル
	private int message_id;
	private String send_by;
	private String msg;
	private Date msg_date;
	
	//userテーブル
	private String user_name;
}
