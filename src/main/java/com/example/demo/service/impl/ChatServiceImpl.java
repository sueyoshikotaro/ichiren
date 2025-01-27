package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.form.ChatForm;
import com.example.demo.form.GroupDetailView;
import com.example.demo.repository.ChatCrudRepository;
import com.example.demo.service.ChatServiceInterface;

public class ChatServiceImpl implements ChatServiceInterface {
	
	@Autowired
	ChatCrudRepository chatCrud;
	
	//チャット相手を設定
	@Override
	public List<GroupDetailView> setChatUser(int school_id, String user_roll) {

		//チャット相手を一覧で格納
		return chatCrud.setChatUser(school_id, user_roll);
	}

	//チャット相手を検索
	@Override
	public List<GroupDetailView> chatPartnerSearch(int school_id, String search, String user_roll) {

		return chatCrud.chatPartnerSearch(school_id, search, user_roll);
	}

	//チャット履歴を表示
	@Override
	public List<ChatForm> getChatHistory(String user_id, String chatUser_id) {
		
		//チャットルーム作成
		boolean flg = chatCrud.existsChatRoom(user_id, "st33334444");
		
		System.out.println(flg);
		
		//チャットルームがすでに存在しない場合チャットルームを作成する
		if(!flg) {
			chatCrud.createChatRoom(user_id, chatUser_id);
			System.out.println("ぞんざいなし！");
		} else {
			
		}
		
		//チャットルーム検索
		int chatRoom_id = chatCrud.chatRoomSearch(user_id, "st33334444");
		
		System.out.println("履歴：" + chatCrud.getChatHistory(chatRoom_id));
		
		return chatCrud.getChatHistory(chatRoom_id);
	}
	
}
