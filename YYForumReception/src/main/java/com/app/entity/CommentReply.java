package com.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentReply implements Serializable {
    private Integer id;
    private Integer fromUserID;
    private Integer toUserID;
    private String fromUserName;
    private String toUserName;
    private String content;
    private Date time;
    private Integer likes;
}
