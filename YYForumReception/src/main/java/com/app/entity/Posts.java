package com.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Posts {
    private Integer id;
    private String title;
    private String detail;
    private Date time;
    private Integer likes;
    private Integer collections;
    private Integer comments;
    private User user;
    private List<PostsIcon> icons;
}
