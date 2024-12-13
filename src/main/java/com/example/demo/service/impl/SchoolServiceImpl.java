package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.School;
import com.example.demo.repository.SchoolCrudRepository;
import com.example.demo.service.SchoolServiceInterface;

@Service
public class SchoolServiceImpl implements SchoolServiceInterface {

	//フィールド	
	@Autowired
	SchoolCrudRepository repo;

	@Override
	public List<School> schoolDetails() {

		List<School> list = repo.selectSchoolDetails();

		return list;
	}

}
