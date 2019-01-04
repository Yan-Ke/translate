package com.sian.translate.management.user.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


/***
 * 后台管理员信息
 */
@Entity
@Data
public class ManageUserInfo {



    /**主键id**/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**登录名**/
    private String account;

    /**电话号码**/
    private String phone;

    /**用户姓名*/
    private String username;

    /**密码**/
    @JsonIgnore
    private String password;

    /**用户头像**/
    private String headImage;

    /**角色id**/
    @JsonIgnore
    private Integer role;

    /**角色名称**/
    private String roleName;

    /**0正常1禁用**/
    private Integer userStatus;

    /**创建人id**/
    @JsonIgnore
    private Integer createUser;

    /**修改人id**/
    @JsonIgnore
    private Integer updateUser;


    @Transient
    private List<ManageResource> manageResourceList;

    /**创建用户时间**/
    @JsonIgnore
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**更新时间**/
    @JsonIgnore
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
