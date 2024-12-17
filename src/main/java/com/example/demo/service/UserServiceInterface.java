package com.example.demo.service;

import com.example.demo.entity.User;

public interface UserServiceInterface {
	
	
	 	//@Autowired
	 	//UserCrudRepository UserCrudRepo;

		//サービスメソッド
		
		// ユーザ一覧表示
		public Iterable<User> userList();
			// return userCrudRepository.findAll()
		
		
		/*
		 * 坂本
		 * ユーザ
		 * ID重複をチェック
		 */
		public boolean userIdCheck(String user_id);
	 
		/**
		 * 坂本
		 * 管理者
		 * admin判定
		 */
		public String adminCheck(String user_id, String user_pass);
}
