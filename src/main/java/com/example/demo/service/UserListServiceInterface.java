package com.example.demo.service;

import com.example.demo.entity.User;

public interface UserListServiceInterface {
	
	
	 	//@Autowired
	 	//UserCrudRepository UserCrudRepo;

		//サービスメソッド
		
		// ユーザ一覧表示
		public Iterable<User> userList();
			// return userCrudRepository.findAll()
}
