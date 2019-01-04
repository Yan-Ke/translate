package com.sian.translate.management.log.enity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class ManageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    /**用户id**/
    private  Integer userId;

    /**登录名**/
    private String account;

    /**操作内容**/
    private String content;

    /**请求地址**/
    @JsonIgnore
    private String uri;

    /**请求ip**/
    @JsonIgnore
    private  String ip;

    /**请求参数**/
    @JsonIgnore
    private  String paramet;

    /**用户ip**/
    @JsonIgnore
    private String userIp;

    /** 类型 1删除 2修改 3新增 4其他**/
    @JsonIgnore
    private  Integer type;

    /**创建时间**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
