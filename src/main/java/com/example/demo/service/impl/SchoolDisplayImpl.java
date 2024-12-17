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

}
