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
    @JsonIgnore
    private Date createTime;

    /**用户昵称**/
    private String nickName;

    /**vip图标**/
    @Transient
    private String vipIcon;

    /**用户头像**/
    @Transient
    private String headImage;

}
