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
	@Query("insert into notice(notice_id, title, contact_msg, send_date, view_count, send_by, group_id) values(:notice_id, :title, :contact_msg, :send_date, :view_count, :send_by, :group_id)")
	public void noticeRegist();
	
}
