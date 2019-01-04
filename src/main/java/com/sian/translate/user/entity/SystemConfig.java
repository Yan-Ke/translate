package com.sian.translate.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;


/****
 * 关于我们、注册协议等配置项
 */
@Entity
@Data
public class SystemConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;
    /**电话**/
    private String phone;
    /**内容**/
    private String content;

    /**创建人**/
    @JsonIgnore
    private  Integer createUser;
    /**修改人**/
    @JsonIgnore
    private  Integer updateUser;
    /**创建时间**/
//    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private  Date createTime;
    /**修改时间**/
    @JsonIgnore
    private Date updateTime;
    /**1关于我们 2注册协议**/
    @JsonIgnore
    private  Integer type;





}
