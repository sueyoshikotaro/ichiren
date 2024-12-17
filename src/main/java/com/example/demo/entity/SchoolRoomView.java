package com.example.demo.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolRoomView {

	@Id
    private Long schoolId;
    
    private String schoolName;
    private String schoolAbb;
    
    private String roomName;
    private Integer pcFlg;
    private String hall;
    private String floor;
}
