package com.example.demo.Form;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolDisplay {
	@Id
	private Integer school_id;
	private String school_abb;
	private String school_name;
	
	private Integer room_id;
	private String room_name;
	private Integer pc_flg;
	private String hall;
	private String floor;
}
