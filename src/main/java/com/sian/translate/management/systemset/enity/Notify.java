package com.sian.translate.management.systemset.enity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class Notify {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    /**标题**/
    private String title;
    /**内容**/
    private String content;

    /**语言类型**/
    private  Integer languageType;

    /**创建人**/
    @JsonIgnore
    private  Integer createUser;
    /**修改人**/
    @JsonIgnore
    private  Integer updateUser;
    /**创建时间**/
//    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**修改时间**/
//    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
