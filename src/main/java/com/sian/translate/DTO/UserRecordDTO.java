package com.sian.translate.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/****
 * 会员购买流水
 */
@Data
public class UserRecordDTO implements Serializable {

    private int id;

    /**用户ID**/
    private int userId;

    /**支付金额**/
    private BigDecimal amount;

    /**购买月数**/
    private int month;

    /**购买时间**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    /**优惠券ID**/
    private String couponId;

    /**优惠券抵扣金额**/
    private BigDecimal reduceAmount;

    /**0未支付 1一已支付 2支付失败**/
    private int status;

    /**生成的订单ID**/
    private String orderId;

    /**1支付宝 2微信**/
    private int payType;

    /**用户昵称**/
    private String nickName;

    public UserRecordDTO() {
    }
    public UserRecordDTO(int id, int userId, BigDecimal amount, int month, Date payTime, String couponId, BigDecimal reduceAmount, int status, String orderId, int payType, String nickName) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.month = month;
        this.payTime = payTime;
        this.couponId = couponId;
        this.reduceAmount = reduceAmount;
        this.status = status;
        this.orderId = orderId;
        this.payType = payType;
        this.nickName = nickName;
    }
}
