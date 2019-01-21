package com.sian.translate.information.enity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class UserMidInformation {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    /**用户id**/
    private  Integer userId;

    /**资讯id**/
    private  Integer infomationId;

    /**类型 1阅读 2赞**/  Integer type;

    private Date createTime;


}
