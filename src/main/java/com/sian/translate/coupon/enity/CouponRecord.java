package com.sian.translate.coupon.enity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/***
 * 会员发送记录
 */
@Entity
@Data
public class CouponRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;


    /**优惠券id**/
    private Integer couponId;

    /**优惠券名称**/
    private String name;

    /**会员类型 0 新会员 1老会员**/
    private Integer memberType;

    /**指定会员的手机号码逗号为分隔符**/
    private String memberPhones;

    /**0指定会员 1会员类型 2全部**/
    private Integer type;

    /**发送会员人的id**/
    private Integer lssuerId;

    /**发放日期日期**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date grantTime;


}
