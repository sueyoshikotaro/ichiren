package com.example.demo.form;

import lombok.Data;

@Data
public class TeamsDisplay {

	//user_detailのデータ
	private String user_id;
	private int group_id;
	private String user_roll;
	private int score;
	
	
	//teamsのデータ
	private String group_name;
	private String school_name;
	private int group_flg;
	private String genre;
	private String work_status;
	private String all_progress;
	private String est_year;
}
