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

    /**中文**/
    @JsonIgnore
    private String chinese;

    /**藏语**/
    @JsonIgnore
    private String zang;

    /**梵语**/
    @JsonIgnore
    private String sanskirt;

    /**日语**/
    @JsonIgnore
    private String japanese;

    /**英语**/
    @JsonIgnore
    private String english;

    /**翻译内容统一返回该字段**/
    @Transient
    private String content;

    /**创建人用户id**/
    @JsonIgnore
    private  Integer userId;

    /**创建时间**/
    @JsonIgnore
    private Date createTime;

    /**更新时间**/
    @JsonIgnore
    private Date updateTime;


}
