package com.sian.translate.member.enity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/****
 * 会员资费
 */
@Entity
@Data
public class MemberConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    /**会员资费说明藏语**/
    @JsonIgnore
    private String explainChinese;

    /**会员资费说明藏语*/
    @JsonIgnore
    private String explainZang;

    /**会员资费说明 返回该字段给app*/
    @Transient
    private String explain;

    /**资费金额**/
    private BigDecimal amount;

    /**月数**/
    private Integer month;

    /**创建时间**/
//    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    @JsonIgnore
    private Date createTime;

    /**修改时间**/
//    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    @JsonIgnore
    private Date updateTime;

    /**创建人ID**/
    @JsonIgnore
    private String createUser;

    /**修改人ID**/
    @JsonIgnore
    private String updateUser;

}
