package com.example.demo.service;

import java.util.List;

import com.example.demo.form.SchoolDisplay;

public interface SchoolDisplayServiceInterface {
	
	//学校情報詳細一覧
	public List<SchoolDisplay> SchoolDetails(int school_id);

	//編集する学校情報詳細
	public List<SchoolDisplay> EditSchoolDetails(int room_id, int school_id);
	
	//学校情報詳細編集
	public void EditSchoolDetailsComp(String room_name, int pc_flg,String hall, String floor, int school_id, int room_id);
	
	//学校情報詳細追加
	public void AddSchoolDetailsComp(String room_name, int pc_flg,String hall, String floor, int school_id);

	//学校情報削除
	public void DeleteSchoolDetails(int school_id, int room_id);
	
	//同じ教室名が登録されているか検索
	public boolean isExistRoomName(String room_name, int school_id);

}
