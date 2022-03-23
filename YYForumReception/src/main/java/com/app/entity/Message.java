package com.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Integer id;
    private User fromUser;
    private Integer toUserID;
    private String txt;
    private Date time;
    private Integer isRead;
}
