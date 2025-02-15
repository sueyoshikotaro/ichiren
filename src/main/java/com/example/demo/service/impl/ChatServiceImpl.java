package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.form.ChatForm;
import com.example.demo.form.GroupDisplay;
import com.example.demo.repository.ChatCrudRepository;
import com.example.demo.service.ChatServiceInterface;

public class ChatServiceImpl implements ChatServiceInterface {

	@Autowired
	ChatCrudRepository chatCrud;

	//チャット相手を設定
	@Override
	public List<GroupDisplay> setChatUser(int school_id) {

		//チャット相手を一覧で格納
		return chatCrud.setChatUser(school_id);
	}

	//チャット相手を検索
	@Override
	public List<GroupDisplay> chatPartnerSearch(int school_id, String search, String user_roll) {

		return chatCrud.chatPartnerSearch(school_id, search, user_roll);
	}

	//チャット履歴を表示
	@Override
	public List<ChatForm> getChatHistory(String user_id, String chatUser_id) {
		
		//チャットルーム作成
		boolean flg = chatCrud.existsChatRoom(user_id, chatUser_id);

		//チャットルームがすでに存在しない場合チャットルームを作成する
		if (!flg) {
			chatCrud.createChatRoom(user_id, chatUser_id);
		}

		//チャットルーム検索
		int chatRoom_id = chatCrud.chatRoomSearch(user_id, chatUser_id);

		return chatCrud.getChatHistory(chatRoom_id);
	}

	//チャットを送信し、更新後のチャット履歴を格納
	@Override
	public List<ChatForm> sendChat(String user_id, String chatUser_id, String sendText) {

		//チャットルーム検索
		int chatRoom_id = chatCrud.chatRoomSearch(user_id, chatUser_id);
		
		if(sendText != null && !sendText.isEmpty()) {
			//チャット履歴を更新
			chatCrud.updateChat(user_id, chatRoom_id, sendText);
		}
		

		return chatCrud.getChatHistory(chatRoom_id);
	}

	
	/**
	 * リーダ用のサービス
	 */
	//チャット相手を設定
	@Override
	public List<GroupDisplay> leaderSetChatAdmin(int school_id) {
		
		return chatCrud.leaderSetChatUser(school_id);
	}
	
	//チャット相手検索
	@Override
	public List<GroupDisplay> AdminChatPartnerSearch(int school_id, String search) {

		return chatCrud.AdminChatPartnerSearch(school_id, search);
	}
	
	
	/**
	 * メンバ用のサービス
	 */
	//チャット相手を設定
	@Override
	public List<GroupDisplay> memberSetChatUser(int school_id, int group_id) {
		
		return chatCrud.memberSetChatUser(school_id, group_id);
	}

	//チャット相手検索
	@Override
	public List<GroupDisplay> memberChatPartnerSearch(int school_id, int group_id, String search) {
		
		return chatCrud.memberChatPartnerSearch(school_id, group_id, search);
	}

}
