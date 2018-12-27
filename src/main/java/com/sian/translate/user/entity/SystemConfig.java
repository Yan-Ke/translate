package com.sian.translate.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
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

    /**藏语内容**/
    @JsonIgnore
    private String zangContent;
    /**中文内容**/
    @JsonIgnore
    private String chineseContent;

    /**内容统一返回该字段**/
    @Transient
    private String content;
    /**创建人**/
    @JsonIgnore
    private  Integer createUser;
    /**修改人**/
    @JsonIgnore
    private  Integer updateUser;
    /**创建时间**/
    @JsonIgnore
    private  Date createTime;
    /**修改时间**/
    @JsonIgnore
    private Date updateTime;
    /**1关于我们 2注册协议**/
    @JsonIgnore
    private  Integer type;





}
