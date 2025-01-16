package com.example.demo.form;

import lombok.Data;

@Data
public class TeamsDisplay {

	//teamsのデータ
	private int group_id;
	private String group_name;
	private int school_id;
	private String school_name;
	private int group_flg;
	private String genre;
	private String work_status;
	private String all_progress;
	private String est_year;
	
	
	//user_detailのデータ
	private String user_id;
	private String user_roll;
	private int score;
	
	
	//userのデータ
	private String user_name;
}
