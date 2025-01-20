package com.example.demo.form;

import lombok.Data;

@Data
public class TdlistForm {
	
	private int tdlist_id;
	private String user_id;
	private String tdlist_content;
	private String importance;
	private int tdlist_flg;
}
