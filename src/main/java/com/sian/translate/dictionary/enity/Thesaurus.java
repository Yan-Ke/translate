package com.sian.translate.dictionary.enity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/***
 * 词条
 */
@Entity
@Data
public class Thesaurus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    /**词典ID**/
    private  Integer dictionaryId;

    @Transient
    private  String dictionaryName;


    /**其中一种语言**/
    private  String contentOne;

    private  String contentTwo;

    /**词典类型 1藏汉 2藏英 3藏日 4藏梵**/
    private Integer type;

    @JsonIgnore
    /**创建人**/
    private  Integer createUser;

    @JsonIgnore
    /**创建时间**/
    private Date createTime;

    @JsonIgnore
    /**修改人**/
    private  Integer updateUser;

    @JsonIgnore
    /**修改时间**/
    private Date updateTime;

    @Transient
    /**翻译后的结果**/
    private String translateResult;

}
