package com.sian.translate.dictionary.enity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


/****
 * 词典
 */
@Entity
@Data
public class Dictionary {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    /**词典名称**/
    private String name;

    /**词典图片**/
    private String image;

    /**词典类型 1藏汉 2藏英 3藏日 4藏梵**/
    private Integer type;

    /**创建人用户id**/
    @JsonIgnore
    private  Integer userId;

    /**创建人用户id**/
    @JsonIgnore
    private  Integer updateUserId;

    /**创建时间**/
    @JsonIgnore
    private Date createTime;

    /**更新时间**/
    @JsonIgnore
    private Date updateTime;

    /**是否会员可见 0不是 1会员可见**/
    private  Integer isMemberVisible;




}
