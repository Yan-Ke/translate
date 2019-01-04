package com.sian.translate.management.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class ManageUserRole {

    /**主键id**/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**资源id**/
    private String resoureId;

    /**角色名称**/
    private String roleName;

    /**0正常1禁用**/
    private Integer status;

    /**创建人id**/
    @JsonIgnore
    private Integer createUser;

    /**更新人id**/
    @JsonIgnore
    private Integer updateUser;

    /**创建时间**/
    @JsonIgnore
    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**更新时间**/
    @JsonIgnore
    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;





}
