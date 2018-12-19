package com.sian.translate.management.user.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/***
 * 后台管理员信息
 */
@Entity
@Data
public class ManageUserInfo {



    /**主键id**/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**登录名**/
    private String account;

    /**电话号码**/
    private String phone;

    /**用户姓名*/
    private String username;

    /**密码**/
    private String password;

    /**角色(1.超级管理员所有权限 2.管理员 3.普通人员)**/
    private Integer role;

    /**创建用户时间**/
    @JsonIgnore
//    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**更新时间**/
    @JsonIgnore
//    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date update_time;
}
