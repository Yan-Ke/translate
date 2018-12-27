package com.sian.translate.dictionary.enity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;


/****
 * 用户收藏记录
 */
@Entity
@Data
public class UserCollectionDictionary {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    /**翻译内容**/
    private String content;

    /**需要翻译的语言类型 0 汉语 1 藏语 2 梵语 3 日语 4 英语**/
    @JsonIgnore
    private Integer formType;

    /**翻译后的语言类型 0 汉语 1 藏语 2 梵语 3 日语 4 英语**/
    @JsonIgnore
    private Integer toType;

    /**翻译后内容**/
    private String translateContent;

    /**创建人用户id**/
    @JsonIgnore
    private  Integer userId;

    /**创建时间**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;



}
