package com.app.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private Integer id;
    private Integer sex;
    private String account;
    private String password;
    private String icon;
    private String name;
    private String sign;
    private Date regTime;
    private Date birthday;
    //是否相互关注
    private Integer isFriend;
    private String backImg;
}
