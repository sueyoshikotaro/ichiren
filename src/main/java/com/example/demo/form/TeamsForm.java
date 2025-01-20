package com.example.demo.form;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class TeamsForm {

	@Id
	private String group_id;
	private String group_name;
	private String school_name;
	private String school_id;
	
	private int group_flg;
	private String genre;
	private String work_status;
	private String all_progress;
	private String est_year;
}
