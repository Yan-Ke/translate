package com.sian.translate.hint.enums;


import lombok.Getter;

@Getter
public enum HintMessageEnum {

    SUCCESS(1,"成功"),
    PASSWORD_NOT_EMPTY(2,"密码不能为空"),
    USER_NOT_EXIST(3,"用户不存在"),
    PASSWORD_ERROR(4,"密码错误"),
    ID_NOT_EMPTY(5,"ID不能为空"),
    PHONE_NOT_EMPTY(6,"手机号码不能为空"),
    IMG_NOT_EMPTY(7,"图片不能为空"),
    IMG_FORMAT_ERROR(8,"图片解析失败"),
    FEED_BACK_NOT_EMPTY(9,"反馈内容不能为空"),
    FEED_BACK_TO_LONG(10,"反馈内容不能超过300字符"),
    MEMBER_NOT_EMPTY(11,"会员类型不存在"),
    MEMBER_CONFIG_ERROR(12,"该会员配置信息有误"),
    COUPON_NOT_EXIST(13,"该优惠卷不存在"),
    COUPON_INFO_INCOMPLETE(14,"优惠卷信息不全"),
    COUPON_OVERDUE(15,"该优惠已经过期"),
    COUPON_USEED(16,"该优惠已使用"),
    COUPON_UNSATISFIED(17,"未满足优惠卷使用金额"),
    COUPON_OVER_TOTAL(18,"该优惠卷抵扣金额大于了总金额"),
    CONTENT_NOT_EMPTY(19,"需要翻译内容不能为空"),

    /**后台提示信息 不需要藏语**/
    ACCOUNT_NOT_EMPTY(1001,"登录名不能为空"),
    NOT_LOGIN(1002,"登陆已过期或未登陆,请登陆。"),

    ;

    private  Integer code;

    private String message;


    HintMessageEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
