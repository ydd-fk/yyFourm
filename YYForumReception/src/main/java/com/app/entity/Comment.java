package com.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private Integer id;
    private Integer userID;
    private Integer postsID;
    private String content;
    private String userName;
    private String userIcon;
    private Date time;
    private Integer replayCount;
    private Integer likes;
    private List<CommentReply> replyList;
    private Integer isLike;
}
