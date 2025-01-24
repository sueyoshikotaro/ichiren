package com.example.demo.service;

import java.util.List;

import com.example.demo.form.ChatForm;
import com.example.demo.form.GroupDetailView;

public interface ChatServiceInterface {
	
	//チャット相手を設定
	public List<GroupDetailView> setChatUser(int school_id, String user_roll);

	//チャット相手検索
	public List<GroupDetailView> chatPartnerSearch(int school_id, String search, String user_roll);
	
	//チャット履歴を表示
	public List<ChatForm> getChatHistory(String user_id, String chatUser_id);

}
