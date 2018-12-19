package com.sian.translate.information.enity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Information {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    /**主题**/
    private String theme;

    /**内容**/
    private String content;

    /**小图**/
    private String smallImage;

    /**大图**/
    private String bigImage;

    /**原图**/
    private String orignalImage;

    /**发布人*/
    private String createUser;

    /**发布时间**/
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**更新时间**/
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**类型分组用(预留字段)**/
    @JsonIgnore  //返回时排除掉这个字段
    private  Integer type;

}
