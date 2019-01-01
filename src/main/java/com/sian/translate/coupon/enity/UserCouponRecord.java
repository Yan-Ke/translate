package com.sian.translate.coupon.enity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/***
 * 用户优惠券领取记录
 */
@Entity
@Data
public class UserCouponRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;


    /**优惠券id**/
    private Integer couponId;

    /**用户优惠券中间表id**/
    private Integer userMidCouponId;


    /**优惠券名称**/
    private String couponName;

    /**用户id**/
    private Integer userId;

    /**用户手机号**/
    private String userPhone;

    /**用户昵称**/
    private String userName;

    /**用户头像**/
    private String userHead;

    /**0不是会员 1是会员 2过期会员**/
    private Integer isMember;

    /**获得日期**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date receiveTime;

    /**使用日期**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date useTime;

    /**使用订单号**/
    private String useOrderNo;



}
