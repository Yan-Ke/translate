package com.sian.translate.user.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
//@Table(name = "user_info")
public class UserInfo {


    /**主键id**/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**用户昵称**/
    private String nickName;
    /**用户密碼**/
    private String password;
    /**用户微信qq唯一id**/
    private String openid;
    /**用户头像原图**/
    private String orignalImage;
    /**用户头像小图**/
    private String headSmallImage;
    /**用户头像大图**/
    private String headBigImage;
    /**用户电话**/
    private String phone;
    /**用户性别 0未知1男2女3保密**/
    private Integer sex;
    /**用户年龄**/
    private Integer age;
    /**用户学历**/
    private String education;
    /**用户余额**/
    private BigDecimal  balance;
    /**用户注册时间**/
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date registrationTime;
    /**用户更新时间**/
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    /**用户会员开始时间**/
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date memberBeginTime;
    /**用户会员截止时间**/
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date memberEndTime;
    /**是否为会员 0 不是 1是**/
    @Transient
    private Integer isMember;
}
