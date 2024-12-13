package com.example.demo.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
	
	@Id
	private Integer room_id;
	private String room_name;
	private Integer pc_flg;
	private String hall;
	private String floor;
	private int school_id;
}
