package com.sian.translate.VO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Created by 廖师兄
 * 2017-12-10 18:02
 */
@Data
public class ResultVO<T> {

    private Integer code;

    private String msg;

    @JsonIgnore
    private String logmsg;

    @JsonInclude(value= JsonInclude.Include.NON_NULL)
    private T data;
}
