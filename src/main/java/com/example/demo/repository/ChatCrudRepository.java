package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.form.ChatForm;
import com.example.demo.form.GroupDisplay;

public interface ChatCrudRepository extends CrudRepository<ChatForm, Integer> {
	
	/*
	 * 末吉
	 * チャット相手を格納
	 */
	@Query("select distinct u.user_id, u.user_name, t.group_name from teams t join user_detail ud on t.group_id = ud.group_id join user u on ud.user_id = u.user_id where t.school_id = :school_id and ud.user_roll = 'リーダ' and t.group_flg = 1 group by u.user_id")
	public List<GroupDisplay> setChatUser(int school_id);

	/**
	 * 末吉
	 * チャット相手を検索
	 */
	@Query("select * from teams t join user_detail ud on t.group_id = ud.group_id join user u on ud.user_id = u.user_id where t.school_id = :school_id and ud.user_roll = :user_roll and t.group_flg = 1 and u.user_name like concat('%', :search, '%')")
	public List<GroupDisplay> chatPartnerSearch(int school_id, String search, String user_roll);

	/**
	 * チャットルームがすでに存在するかどうか確認
	 */
	@Query("select exists(select 1 from chatroom where (user_id1 = :user_id and user_id2 = :chatUser_id) or (user_id1 = :chatUser_id and user_id2 = :user_id))")
	public boolean existsChatRoom(String user_id, String chatUser_id);
	
	/**
	 * 末吉
	 * チャットルーム作成
	 */
	@Modifying
	@Query("insert into chatroom(user_id1, user_id2) values(:user_id, :chatUser_id)")
	public void createChatRoom(String user_id, String chatUser_id);
	
	/**
	 * 末吉
	 * チャットルーム検索
	 */
	@Query("select chat_id from chatroom where (user_id1 = :user_id and user_id2 = :chatUser_id) or (user_id1 = :chatUser_id and user_id2 = :user_id)")
	public int chatRoomSearch(String user_id, String chatUser_id);
	
	/**
	 * 末吉
	 * チャット履歴を表示
	 */
	@Query("select * from message m join user u on m.send_by = u.user_id where chat_id = :chatRoom_id")
	public List<ChatForm> getChatHistory(int chatRoom_id);
	
	/**
	 * 末吉
	 * チャット履歴を更新
	 */
	@Modifying
	@Query("insert into message(send_by, chat_id, msg) values(:user_id, :chatRoom_id, :sendText)")
	public void updateChat(String user_id, int chatRoom_id, String sendText);

	
	/**
	 * リーダ用
	 */
	
	/**
	 * 末吉
	 * チャット相手を格納
	 */
	@Query("select * from user where school_id = :school_id and user_id like 'te%'")
	public List<GroupDisplay> leaderSetChatUser(int school_id);
	
	/**
	 * 末吉
	 * チャット相手を検索
	 */
	@Query("select * from user where school_id = :school_id and user_id like 'te%' and user_name like concat('%', :search, '%')")
	public List<GroupDisplay> AdminChatPartnerSearch(int school_id, String search);
	
	
	/**
	 * ユーザ用
	 */
	
	/**
	 * 末吉
	 * チャット相手を格納
	 */
	@Query("select * from teams t join user_detail ud on t.group_id = ud.group_id join user u on ud.user_id = u.user_id where t.school_id = :school_id and ud.group_id = :group_id and t.group_flg = 1")
	public List<GroupDisplay> memberSetChatUser(int school_id, int group_id);
	
	/**
	 * 末吉
	 * チャット相手を検索
	 */
	@Query("select * from teams t join user_detail ud on t.group_id = ud.group_id join user u on ud.user_id = u.user_id where t.school_id = :school_id and ud.group_id = :group_id and t.group_flg = 1 and u.user_name like concat('%', :search, '%')")
	public List<GroupDisplay> memberChatPartnerSearch(int school_id, int group_id, String search);
	
}
