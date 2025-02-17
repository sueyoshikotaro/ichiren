package com.example.demo.form;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class SchoolDisplay {
	@Id
	private int school_id;
	private String school_abb;
	private String school_name;
	
	private int room_id;
	private String room_name;
	private String before_room_name;
	private int pc_flg;
	private String hall;
	private String floor;
}
