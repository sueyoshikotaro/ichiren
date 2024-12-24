package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.form.SchoolDisplay;
import com.example.demo.repository.SchoolDisplayCrudRepository;
import com.example.demo.service.SchoolDisplayServiceInterface;

public class SchoolDisplayImpl implements SchoolDisplayServiceInterface {

	@Autowired
	SchoolDisplayCrudRepository repo;
	
	@Override
	public List<SchoolDisplay> SchoolDetails() {
		
		
		return repo.selectSchoolDetails();
	}

	@Override
	public List<SchoolDisplay> EditSchoolDetails(String room_id) {
		
		return repo.schoolDetailsChange(room_id);
	}

	@Override
	public void EditSchoolDetailsComp(String room_name, int pc_flg,String hall, String floor, int school_id, int room_id) {
		
		repo.EditSchoolDetailsComp(room_name, pc_flg, hall, floor, school_id, room_id);
	}

	@Override
	public void AddSchoolDetailsComp(String room_name, int pc_flg, String hall, String floor, int school_id) {
		
		repo.AddSchoolDetailsComp(room_name, pc_flg, hall, floor, school_id);
		
	}

}