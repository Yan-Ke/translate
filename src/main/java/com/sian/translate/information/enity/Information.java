package com.sian.translate.information.enity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Information {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    /**标题**/
    private String title;

    /**内容**/
    private String content;

    /**作者**/
    private String author;

    /**跳转地址**/
    private String url;

    /**原图**/
    private String image;

    /**发布人*/
    private Integer createUser;

    /**发布人*/
    private Integer updateUser;

    /**发布时间**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**更新时间**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**0 文章 1广告**/
    private Integer type;

    /**语言类型**/
    @JsonIgnore  //返回时排除掉这个字段
    private  Integer languageType;

    /**排序 数字越大越靠前**/
    @JsonIgnore  //返回时排除掉这个字段
    private  Integer order;

    /**0隐藏1显示**/
    @JsonIgnore  //返回时排除掉这个字段
    private  Integer isShow;




}
