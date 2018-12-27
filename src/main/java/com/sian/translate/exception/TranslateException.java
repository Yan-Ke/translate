package com.sian.translate.exception;

import com.sian.translate.hint.enums.HintMessageEnum;

public class TranslateException extends RuntimeException {

    private Integer code;

    public TranslateException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public TranslateException(HintMessageEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }
}