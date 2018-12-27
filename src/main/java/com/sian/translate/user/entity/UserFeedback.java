package com.sian.translate.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/***
 * 帮助与反馈
 */
@Data
@Entity
public class UserFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    /**用户id**/
    private  Integer userId;

    /**反馈内容**/
    private String content;

    /**反馈时间**/
    private Date createTime;

    /**用户头像**/
    private String userHead;

    /**用户手机**/
    private String userPhone;

    /**用户昵称**/
    private String userNickName;

    /**状态 0 未处理 1已处理**/
    private Integer status;

    /**图片地址**/
    @JsonIgnore
    private String image;

    @Transient
    private List<String> images = new ArrayList<>();






}
