package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.form.NoticeViewForm;

public interface NoticeCrudRepository extends CrudRepository<NoticeViewForm, String> {

	/*
	 * 向江
	 * 連絡事項一覧
	 */
	@Query("SELECT n.notice_id,n.title,n.contact_msg,n.send_date,n.view_count, user_name"
			+ " FROM notice AS n INNER JOIN user ON user_id=send_by WHERE group_id=:group_id order by n.notice_id desc")
	public List<NoticeViewForm> noticeDisp(int group_id);
	
	
	/*
	 * 向江
	 * 連絡事項作成登録
	 */
	@Modifying
	@Query("insert into notice(title, contact_msg, view_count, send_by, group_id) values (:title, :contact_msg, :view_count, (select user_id from user where user_id = :send_by), :group_id)")
	public void noticeRegist(String title, String contact_msg, int view_count, String send_by, int group_id);

	
	/*
	 * 向江
	 * 連絡事項削除
	 */
	@Modifying
	@Query("delete from notice where notice_id=:notice_id")
	public void noticeDelete(int notice_id);


	@Query("select *,user.user_name from notice inner join user on send_by = user_id where notice_id=:notice_id")
	public List<NoticeViewForm> selectNotice(int notice_id);
}
