package com.sian.translate.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/****
 * 用户支付时可使用的优惠券
 */
@Data
public class UserCouponDTO implements Serializable {

    private int id;

    /**优惠券名称**/
    private String name;

    /**截止日期**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;

    /**截止日期**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**优惠券使用金额**/
    private BigDecimal fullPrice;

    /**优惠券抵扣金额**/
    private BigDecimal reducePrice;

    /**优惠券ID**/
    private int couponId;


    public UserCouponDTO() {
    }

    public UserCouponDTO(int id, String name, Date beginTime,Date endTime, BigDecimal fullPrice, BigDecimal reducePrice,int couponId) {
        this.id = id;
        this.name = name;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.fullPrice = fullPrice;
        this.reducePrice = reducePrice;
        this.couponId = couponId;
    }
}
