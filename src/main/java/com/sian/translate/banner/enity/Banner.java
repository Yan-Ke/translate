package com.sian.translate.banner.enity;

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
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    /**内容**/
    private String content;

    /**跳转链接**/
    private String url;

    /**原图**/
    private String image;

    /**停留时间单位秒(预留字段)**/
    @JsonIgnore  //返回时排除掉这个字段
    private  Integer time;

    /**类型分组用(预留字段)**/
    @JsonIgnore  //返回时排除掉这个字段
    private  Integer type;

    /**创建人id**/
    @JsonIgnore  //返回时排除掉这个字段
    private  Integer createUser;

    /**修改人id**/
    @JsonIgnore  //返回时排除掉这个字段
    private  Integer updateUser;

    /**创建日期**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**修改日期**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;





}
