package com.example.demo.service;

import java.util.Collection;
import java.util.List;

import com.example.demo.entity.Teams;

public interface GroupDisplayServiceInterface {

	// グループ一覧表示
	public List<Teams> groupList(String estYear);
	
	// 発足年度で絞り込みする
	public Collection<Teams> findDistinctEstYear();
}
