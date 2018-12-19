package com.sian.translate.user.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;


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
}
