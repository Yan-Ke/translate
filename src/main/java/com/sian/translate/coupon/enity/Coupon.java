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

    /**优惠券名称**/
    private String name;

    /**优惠券内容**/
    private String content;

    /**满足此金额使用**/
    private BigDecimal fullPrice;

    /**抵扣金额**/
    private BigDecimal reducePrice;


    /**是否过期 0未过期 1过期**/
    @Transient
    private Integer overdue;

    /**优惠卷开始使用日期**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;

    /**优惠卷截止使用日期**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**发布人*/
    @JsonIgnore
    private Integer createUser;

    /**更新人*/
    @JsonIgnore
    private Integer updateUser;

    /**优惠卷创建时间**/
    @JsonIgnore
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**优惠卷更新时间**/
    @JsonIgnore
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**剩余数量**/
    private  Integer count;

    /**0 按开始日期到截止计算优惠券周期 1 按几天计算日期，从领取日开始计算**/
    private  Integer type;

    /**几天内有效(从领取时开始计算),type=1时不能为0**/
    private  Integer day;





}
