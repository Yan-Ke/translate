package com.sian.translate.banner.enity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

    /**小图**/
    private String smallImage;

    /**大图**/
    private String bigImage;

    /**原图**/
    private String orignalImage;

    /**停留时间单位秒(预留字段)**/
    @JsonIgnore  //返回时排除掉这个字段
    private  Integer time;

    /**类型分组用(预留字段)**/
    @JsonIgnore  //返回时排除掉这个字段
    private  Integer type;



}
