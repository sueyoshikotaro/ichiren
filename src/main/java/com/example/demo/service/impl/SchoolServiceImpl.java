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

	//学校情報管理システム
	/**
	 * 末吉
	 * 学校情報詳細表示
	 */
	@Override
	public List<School> schoolDetails() {
		
		List<School> list = repo.selectSchoolDetails();

		return list;
	}

	
	/**
	 * 末吉
	 * 教室情報検索
	 */
//	@Override
//	public List<School> existRoom(String room_name) {
//		
//		List<School> list = repo.serchRoom(room_name);
//		
//		System.out.println(repo.serchRoom(room_name).toString());
//		
//		if(list == null || list.isEmpty()) {
//			
//			list = repo.selectSchoolDetails();
//			return list;
//			
//		} else {
//			
//			return list;
//		}
//		return null;
//		
//	}

	

}
