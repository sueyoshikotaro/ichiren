package com.example.demo.form;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Room {

	@Id
	private int room_id;
	private String room_name;
	private int pc_flg;
	private String hall;
	private String floor;
	private int school_id;
}
