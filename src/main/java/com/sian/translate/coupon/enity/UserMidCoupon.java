package com.sian.translate.coupon.enity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/****
 * 用户和优惠券中间表
 */
@Data
@Entity
public class UserMidCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    /**用户id**/
    private  Integer userId;

    /**优惠券id**/
    private  Integer couponId;

    /**状态 0未使用 1已经使用 **/
    private  Integer isUse;

    /**优惠券开始日期**/
    private Date beginTime;

    /**优惠券开始日期**/
    private Date endTime;

}
