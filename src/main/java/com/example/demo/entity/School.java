package com.example.demo.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * DBアクセス用のEntityクラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class School {

	@Id
	private Integer school_id;
	private String school_abb;
	private String school_name;
	

}

