package com.example.demo.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tdlist {
	@Id
	private int tdlist_id;
	private String user_id;
	private String tdlist_content;
	private String importance;
	private int tdlist_flg;
}
