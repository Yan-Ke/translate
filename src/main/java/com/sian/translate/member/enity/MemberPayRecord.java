package com.sian.translate.member.enity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/****
 * 会员购买记录
 */
@Entity
@Data
@Table(name = "user_order")
public class MemberPayRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    /**生成的订单ID**/
    private String orderId;

    /**用户ID**/
    private Integer userId;

    /**优惠券ID**/
    private Integer couponId;

    /**用户优惠券中间表id**/
    private Integer userMidCouponId;

    /**支付金额**/
    private BigDecimal amount;

    /**实际金额**/
    @Transient
    private BigDecimal actualAmount;

    /**优惠券抵扣金额**/
    private BigDecimal reduceAmount;

    /**购买月数**/
    private Integer month;

    @Transient
    private String monthString;

    /**0未支付 1一已支付 2支付失败**/
    private Integer status;

    /**1支付宝 2微信**/
    private Integer payType;

    /**购买时间**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

}
