package com.example.demo.service;

import java.util.List;

import com.example.demo.form.ChatForm;
import com.example.demo.form.GroupDetailView;

public interface ChatServiceInterface {

	//チャット相手を設定
	public List<GroupDetailView> setChatUser(int school_id);

	//チャット相手検索
	public List<GroupDetailView> chatPartnerSearch(int school_id, String search, String user_roll);

	//チャット履歴を表示
	public List<ChatForm> getChatHistory(String user_id, String chatUser_id);

	//チャットを送信し、更新後のチャット履歴を格納
	public List<ChatForm> sendChat(String user_id, String chatUser_id, String sendText);

	/**
	 * ユーザ用チャット
	 */
	//チャット相手を設定
	public List<GroupDetailView> memberSetChatUser(int school_id, int group_id);

	//チャット相手検索
	public List<GroupDetailView> memberChatPartnerSearch(int school_id, int group_id, String search);

}
