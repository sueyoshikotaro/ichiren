package com.example.demo.service;

import java.util.List;

import com.example.demo.form.SchoolDisplay;

public interface SchoolDisplayServiceInterface {
	
	public List<SchoolDisplay> SchoolDetails();

	public List<SchoolDisplay> EditSchoolDetails(String room_id);
}
