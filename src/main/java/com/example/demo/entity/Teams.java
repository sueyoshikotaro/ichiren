package com.example.demo.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Teams {

	@Id
	private int group_id;
	private String group_name;
	private String school_id;
	private int group_flg;
	private String genre;
	private String work_status;
}
