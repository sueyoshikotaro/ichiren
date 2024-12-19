package com.example.demo.form;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolDisplay {
	@Id
	private int school_id;
	private String school_abb;
	private String school_name;
	
	private int room_id;
	private String room_name;
	private int pc_flg;
	private String hall;
	private String floor;
}
