package com.sian.translate.management.user.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class ManageResource {

    /**主键id**/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**相应模块名称**/
    private String resourceName;

    /**对应的父模块名称**/
    private String parentName;




}
