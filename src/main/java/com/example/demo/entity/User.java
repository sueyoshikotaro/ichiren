package com.example.demo.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	private String user_id;
	private String user_name;
	private String user_pass;
	private String school_id;
	private String enr_year;
	private String user_flg;

}
