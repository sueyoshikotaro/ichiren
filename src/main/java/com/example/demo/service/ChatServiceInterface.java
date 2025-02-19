package com.example.demo.service;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import com.example.demo.form.ChatForm;
import com.example.demo.form.GroupDisplay;

public interface ChatServiceInterface {

	//チャット相手を設定
	public List<GroupDisplay> setChatUser(int school_id);

	//チャット相手検索
	public List<GroupDisplay> chatPartnerSearch(int school_id, String search, String user_roll);

	//チャット履歴を表示
	public List<ChatForm> getChatHistory(String user_id, String chatUser_id);

	//チャットを送信し、更新後のチャット履歴を格納
	public List<ChatForm> sendChat(String user_id, String chatUser_id, String sendText);

	
	/**
	 * リーダ用のサービス
	 */
	//チャット相手(管理者)を設定
	public List<GroupDisplay> leaderSetChatAdmin(int school_id);
	
	//チャット相手検索
	public List<GroupDisplay> AdminChatPartnerSearch(int school_id, String search);

	
	/**
	 * メンバ用のサービス
	 */
	//チャット相手を設定
	public List<GroupDisplay> memberSetChatUser(int school_id, int group_id);

	//チャット相手検索
	public List<GroupDisplay> memberChatPartnerSearch(int school_id, int group_id, String search);

	/**
	 * 共通のサービス
	 */
	public boolean CookieCheck(HttpServletRequest request);
}
