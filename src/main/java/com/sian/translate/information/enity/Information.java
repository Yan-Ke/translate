package com.sian.translate.information.enity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
//    @JsonIgnore  //返回时排除掉这个字段
    private  Integer languageType;

    /**排序 数字越大越靠前**/
//    @JsonIgnore  //返回时排除掉这个字段
    private  Integer infomationOrder;

    /**0隐藏1显示**/
//    @JsonIgnore  //返回时排除掉这个字段
    private  Integer isShow;

    /**阅读量**/
    private  Integer lookCount;
    /**赞数量**/
    private  Integer zan;

    /**是否赞 0 没有赞 1已经赞过**/
    @Transient
    private  Integer isZan;


    @Transient
    private String createTimeString;


    /** 广告位置 1首页2词库翻译页3在线翻译页4离线资源**/
    private  Integer advLocation;

    /**是否推荐 0 不是 1是**/
    private  Integer recommend;



}
