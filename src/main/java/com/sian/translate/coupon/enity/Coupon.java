package com.sian.translate.coupon.enity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    /**优惠券名称中文**/
    @JsonIgnore
    private String chineseName;
    /**优惠券名称藏文**/
    @JsonIgnore
    private String zangName;
    /**优惠券名称 返回该字段给客户端**/
    @Transient
    private String name;

    /**优惠券内容中文**/
    @JsonIgnore
    private String chineseContent;
    /**优惠券内容*藏文*/
    @JsonIgnore
    private String zangContent;
    /**优惠券内容 返回该字段给客户端**/
    @Transient
    private String content;

    /**优惠券图片链接**/
    private String image;

    /**满足此金额使用**/
    private BigDecimal fullPrice;

    /**抵扣金额**/
    private BigDecimal reducePrice;

    /**状态 0未使用 1已经使用 2已过期**/
    private  Integer status;

    /**是否过期 0未过期 1过期**/
    @Transient
    private Integer overdue;

    /**优惠卷截止使用日期**/
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**发布人*/
    @JsonIgnore
    private String createUser;

    /**更新人*/
    @JsonIgnore
    private String updateUser;

    /**优惠卷创建时间**/
    @JsonIgnore
//    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**优惠卷更新时间**/
    @JsonIgnore
//    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updateTime;



}
