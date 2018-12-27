package com.sian.translate.hint.enums;


import lombok.Getter;

@Getter
public enum HintMessageEnum {

    SUCCESS(1,"成功"),
    PASSWORD_NOT_EMPTY(2,"不能为空"),
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
    COUPON_ID_NOT_EMPTY(20,"优惠券ID不能为空"),
    COUPON_COUNT_NOT_ENOUGH(21,"优惠券数量不足"),
    COUPON_ALREADY_RECEIVE(22,"该优惠券已经领取"),
    CODE_NOT_EMPTY(23,"验证码不能为空"),
    CODE_ERROR(24,"验证码错误"),
    OPENID_NOT_EMPTY(25,"openID不能为空"),
    PHONE_USEED(26,"该手机号码已被使用"),
    NOW_PHONE_IS_NOT_EMPTY(27,"注册手机号码不能为空"),
    NEW_PHONE_IS_NOT_EMPTY(28,"新手机号码不能为空"),
    NOW_PHONE_ERROR(29,"注册手机号码不匹配"),
    USER_PROHIBIT(30,"用户被禁用"),
    SYSTEM_CONFIG_TYPE_NOT_EMPPPTY(31,"类型不能为空"),
    SYSTEM_CONFIG_EMPPPTY(32,"未获取到该配置项"),
    PAY_FORMAT_ERROR(33,"支付金额格式有误"),




    /**后台提示信息 不需要藏语**/
    ACCOUNT_NOT_EMPTY(1001,"登录名不能为空"),
    NOT_LOGIN(1002,"登陆已过期或未登陆,请登陆。"),
    EXPLAIN_CHINESE_NOT_EMPTY(1003,"请输入会员权限说明(中文)"),
    EXPLAIN_ZANG_NOT_EMPTY(1004,"请输入会员权限说明(藏文)"),
    MEMBER_AMOUNT_NOT_EMPTY(1005,"请输入会员资费金额"),
    MEBER_MONTH_NOT_EMPTY(1006,"请输入会员月数"),
    MEBER_CONIFG_ID_NOT_EMPTY(1007,"会员配置ID不能为空"),
    MEBER_CONIFG_IS_NULL(1008,"会员配置不存在"),
    IMPORT_EXCEL_ERROR(1009,"导入的excel文件格式不正确"),

    EXCEL_CHINESE_NOT_EMPTY(1009,"中文不能为空"),
    EXCEL_ZANG_NOT_EMPTY(1010,"藏语不能为空"),
    EXCEL_SANSKIRT_NOT_EMPTY(1011,"梵语不能为空"),
    EXCEL_JAPANESE_EMPTY(1012,"日语不能为空"),
    EXCEL_ENGLISH_NOT_EMPTY(1013,"英语不能为空"),
    EXCEL_CHINESE_EXIST(1014,"中文已经存在"),
    EXCEL_ZANG_EXIST(1015,"藏语已经存在"),
    EXCEL_SANSKIRT_EXIST(1016,"梵语已经存在"),
    EXCEL_JAPANESE_EXIST(1017,"日语已经存在"),
    EXCEL_ENGLISH_EXIST(1018,"英语已经存在"),


    BANNER_CONTENT_NOT_EMPTY(1019,"banner内容不能为空"),
    BANNER_URL_NOT_EMPTY(1020,"banner的url链接不能为空"),
    BANNER_LANGUAGE_NOT_EMPTY(1021,"banner语言类型不能为空"),
    BANNER_ID_NOT_EMPTY(1022,"bannerId不能为空"),
    BANNER_NOT_EXIST(1023,"该条banner记录不存在"),

    INFORMATION_TITLE_NOT_EMPTY(1024,"资讯标题不能为空"),
    INFORMATION_CONTENT_URL_NOT_EMPTY(1025,"资讯内容和外部链接不能同时为空"),
    INFORMATION_TYPE_NOT_EMPTY(1025,"资讯类型不能为空"),
    INFORMATION_LANGUAGETYPE_NOT_EMPTY(1026,"资讯语言类型不能为空"),
    INFORMATION_ID_NOT_EMPTY(1027,"资讯Id不能为空"),
    INFORMATION_NOT_EXIST(1028,"该条资讯记录不存在"),

    COUPON_NAME_NOT_EMPTY(1029,"优惠卷名称不能为空"),
    COUPON_CONTENT_NOT_EMPTY(1030,"优惠卷内容不能为空"),
    COUPON_LANGUAGETYPE_NOT_EMPTY(1031,"优惠券语言类型不能为空"),
    COUPON_FULL_PRICE_NOT_EMPTY(1032,"优惠券使用金额不能为空"),
    COUPON_FULL_PRICE_FORMAT_ERROR(1033,"优惠券使用金额格式错误"),
    COUPON_REDUCE_PRICE_NOT_EMPTY(1034,"优惠券抵扣金额不能为空"),
    COUPON_REDUCE_PRICE_FORMAT_ERROR(1035,"优惠券抵扣金额格式错误"),
    COUPON_END_TIME_NOT_EMPTY(1036,"优惠券截止使用日期不能为空"),
    COUPON_END_TIME_FORMAT_ERROR(1037,"优惠券截止使用日期格式错误"),
    ;

    private  Integer code;

    private String message;


    HintMessageEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
