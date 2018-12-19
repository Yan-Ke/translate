package com.sian.translate.dictionary.enity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


/****
 * 用户单词翻译历史记录
 */
@Entity
@Data
public class UserTranslateRecord {


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
    @JsonIgnore
    private String translateContent;

    /**创建人用户id**/
    @JsonIgnore
    private  Integer userId;

    /**创建时间**/
    @JsonIgnore
    private Date createTime;



}
