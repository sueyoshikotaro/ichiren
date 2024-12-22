package com.example.demo.service;

import java.util.List;

import com.example.demo.form.SchoolDisplay;

public interface SchoolDisplayServiceInterface {
	
	//学校情報詳細一覧
	public List<SchoolDisplay> SchoolDetails();

	//編集する学校情報詳細
	public List<SchoolDisplay> EditSchoolDetails(String room_id);
	
	//学校情報詳細編集
	public void EditSchoolDetailsComp(int room_id, String room_name, int pc_flg,String hall, String floor, int school_id);
}
