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

    /**词典id**/
    private  Integer dictionaryId;

    /**翻译内容**/
    private String content;

    /**词典类型 1藏汉 2藏英 3藏日 4藏梵**/
    private Integer type;

    /**翻译后内容**/
    private String translateContent;

    /**创建人用户id**/
    @JsonIgnore
    private  Integer userId;

    /**创建时间**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


    /**0句子翻译 1单词翻译**/
    private Integer isWord;



}
