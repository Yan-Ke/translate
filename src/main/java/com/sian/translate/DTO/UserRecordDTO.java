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

    private String monthString;


    /**购买时间**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    /**优惠券ID**/
    private Integer couponId;

    /**优惠券抵扣金额**/
    private BigDecimal reduceAmount;

    /**实际金额**/
    private BigDecimal actualAmount;


    /**0未支付 1一已支付 2支付失败**/
    private int status;

    /**生成的订单ID**/
    private String orderId;

    /**1支付宝 2微信**/
    private int payType;

    /**用户昵称**/
    private String nickName;

    /**用户头像**/
    private String headBigImage;

    /**会员开始时间**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date memberBeginTime;

    /**会员结束时间**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date memberEndTime;

    /**优惠券名称**/
    private String couponName;

    private int isMember;

    private String vipIcon;

    /**订单总金额**/
    private BigDecimal totalAmount;

    public UserRecordDTO() {
    }

    public UserRecordDTO(int id, int userId, BigDecimal amount, int month, Date payTime, int couponId, BigDecimal reduceAmount, int status, String orderId, int payType, String nickName, String headBigImage, Date memberBeginTime, Date memberEndTime, String couponName,BigDecimal totalAmount) {
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
        this.headBigImage = headBigImage;
        this.memberBeginTime = memberBeginTime;
        this.memberEndTime = memberEndTime;
        this.couponName = couponName;
        this.totalAmount =totalAmount;
    }


}
