package com.sian.translate.hint.enity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;


/***********
 * 返回的提示信息实体类
 */
@Entity
@Data
public class HintMessage {

    @Id
    private  Integer id;

    /**中文提示信息**/
    private String chineseMessage;

    /**藏语提示信息**/
    private String zangMessage;

    /**获取中文还是藏文 0 中文 1藏文**/
    private Integer languageType;


}
