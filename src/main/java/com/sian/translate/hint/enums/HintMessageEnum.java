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
    TRANSLATE_TYPE_EMPTY(34,"翻译类型不能为空"),
    TRANSLATE_RESULT_EMPTY(35,"还没有收录该单词"),
    INFORMATION_ID_NOT_EMPTY(36,"资讯ID不能为空"),
    INFORMATION_NOT_EXIST(37,"该条资讯记录不存在或被删除"),
    INFORMATION_ALREADY_ZAN(38,"已经赞过该条资讯"),
    ADV_LOCAITION_NOT_EMPTY(39,"请选择广告位置"),
    DICTIONARY_ID_NOT_EMPTY(40,"词典ID不能为空"),
    DICTIONARY_NOT_EXIST(41,"该词典不存在"),
    MEMBER_DOWN(42,"该词典仅允许会员下载"),
    DEVICE_ID_NOT_EMPTY(43,"设备ID不能为空"),
    QQ_IS_BAND(44,"qq已被绑定"),
    WEIXIN_IS_BAND(45,"微信已被绑定"),
    IS_WORD_IS_EMPTY(46,"请选择收藏类型"),




    /**后台提示信息 不需要藏语**/
    ACCOUNT_NOT_EMPTY(1001,"登录名不能为空"),
    NOT_LOGIN(1002,"登陆已过期或未登陆,请登陆。"),
    EXPLAIN_CHINESE_NOT_EMPTY(1003,"请输入会员权限说明"),
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
    BANNER_ID_NOT_EMPTY(1022,"bannerID不能为空"),
    BANNER_NOT_EXIST(1023,"该条banner记录不存在"),

    INFORMATION_TITLE_NOT_EMPTY(1024,"资讯标题不能为空"),
    INFORMATION_CONTENT_URL_NOT_EMPTY(1025,"资讯内容和外部链接不能同时为空"),
    INFORMATION_TYPE_NOT_EMPTY(1025,"资讯类型不能为空"),
    INFORMATION_LANGUAGETYPE_NOT_EMPTY(1026,"资讯语言类型不能为空"),

    COUPON_NAME_NOT_EMPTY(1029,"优惠卷名称不能为空"),
    COUPON_CONTENT_NOT_EMPTY(1030,"优惠卷内容不能为空"),
    COUPON_LANGUAGETYPE_NOT_EMPTY(1031,"优惠券语言类型不能为空"),
    COUPON_FULL_PRICE_NOT_EMPTY(1032,"优惠券使用金额不能为空"),
    COUPON_FULL_PRICE_FORMAT_ERROR(1033,"优惠券使用金额格式错误"),
    COUPON_REDUCE_PRICE_NOT_EMPTY(1034,"优惠券抵扣金额不能为空"),
    COUPON_REDUCE_PRICE_FORMAT_ERROR(1035,"优惠券抵扣金额格式错误"),
    COUPON_END_TIME_NOT_EMPTY(1036,"优惠券截止使用日期不能为空"),
    COUPON_BEGIN_TIME_NOT_EMPTY(1037,"优惠券开始日期不能为空"),
    COUPON_TIME_FORMAT_ERROR(1038,"优惠券使用日期格式错误"),
    COUPON_DAY_NOT_EMPTY(1039,"优惠券有效日期不能为空"),


    DICTIONARY_NAME_NOT_EMPTY(1040,"词典名称不能为空"),
    DICTIONARY_TYPE_NOT_EMPTY(1041,"词典语言不能为空"),
    DICTIONARY_IMAGE_NOT_EMPTY(1042,"词典图片不能为空"),



    THESAURUS_ID_NOT_EMPTY(1043,"词条名称不能为空"),
    THESAURUS_NOT_EXIST(1044,"该条词条记录不存在"),

    COUPON_FULL_PRICE_LESS_ZERO(1045,"优惠券使用金额不能小于0"),
    COUPON_REDUCE_PRICE_LESS_ZERO(1046,"优惠券抵扣金额不能小于0"),
    COUPON_GRANT_TYPE_NOT_EMPTY(1047,"请选择发送类型"),
    COUPON_GRANT_MEMBERPHONE_NOT_EMPTY(1048,"发送类型为指定会员时，会员手机号码不能为空"),
    COUPON_GRANT_MEMBER_TYPE_NOT_EMPTY(1049,"请选择会员类型"),
    COUPON_COUNT_ERROR(1050,"优惠券数量不能为空或小于等于0"),
    COUPON_MEMBER_PHONE_ERROR(1051,"用户手机格式有误"),
    COUPON_IS_NULL(2052,"请选择优惠券"),




    MEMBER_MONTY_AMOUNT_NOT_EMPTY(1052,"请输入会员月资费金额"),
    MEMBER_QUART_AMOUNT_NOT_EMPTY(1053,"请输入会员季度资费金额"),
    MEMBER_HALF_YEAR_AMOUNT_NOT_EMPTY(1054,"请输入会员半年资费金额"),
    MEMBER_YEAR_AMOUNT_NOT_EMPTY(1055,"请输入会员年资费金额"),

    THESAURUS_NOT_EMPTY(1056,"词条文字不能为空"),

    DICTIONARY_THESAURUS_IS_EMPTY(1057,"无词条数据"),

    UPLOAD_EXCEL_EMPTY(1058,"上传的Excel文件为空"),

    PAY_TIME_FORMAT_ERROR(1059,"支付日期格式错误"),

    PAY_RECORD_IS_EMPTY(1060,"无购买记录"),

    ROLE_NAME_IS_NOT_EMPTY(1061,"角色名称不能为空"),
    STATUS_IS_NOT_EMPTY(1062,"状态不能为空"),
    ROLE_NAME_IS_EXIST(1063,"该角色名称已经存在"),

    ROLE_ID_IS_NOT_EMPTY(1064,"请选择角色"),
    ROLE_IS_NOT_EXIST(1065,"该角色不存在"),


    MANAGE_ACCOUNT_NOT_EMPTY(1066,"用户名不能为空"),
    MANAGE_ACCOUNT_LENGTH_ERROR(1067,"用户名长度为4-12位"),
    MANAGE_ACCOUNT_FORMAT_ERROR(1068,"用户名只能位数字或字母组合"),
    MANAGE_USERNAME_NOT_EMPTY(1069,"真实姓名不能为空"),
    MANAGE_ACCOUNT_IS_EXIST(1070,"用户名已存在"),


    FEEDBACK_ID_IS_NOT_NULL(1071,"反馈数据ID不能为空"),
    FEEDBACK_IS_NOT_EXIST(1072,"反馈数据不存在"),
    FEEDBACK_IS_HANDER1072(1073,"反馈数据已经处理"),

    NOTIFY_TITLE_NOT_EMPTY(1074,"通知标题不能为空"),
    NOTIFY_CONTENT_NOT_EMPTY(1075,"通知内容不能为空"),
    NOTIFY_ID_NOT_EMPTY(1076,"通知ID不能为空"),
    NOTIFY_IS_NOT_EXIST(1077,"通知不存在"),


    HELP_TITLE_NOT_EMPTY(1078,"帮助标题不能为空"),
    HELP_CONTENT_NOT_EMPTY(1079,"帮助内容不能为空"),
    HELP_ID_NOT_EMPTY(1080,"帮助ID不能为空"),
    HELPIS_NOT_EXIST(1081,"帮助不存在"),
    HELP_STATUS_NOT_EMPTY(1082,"帮助状态不能为空"),


    REGISTE_PROTOCOL_CONTENT_NOT_EMPTY(1083,"注册协议内容不能为空"),

    ABOUT_ME_CONTENT_NOT_EMPTY(1084,"关于内容不能为空"),
    ABOUT_ME_PHONE_NOT_EMPTY(1085,"关于我们手机号码不能为空"),


    DAY_NOT_EMPTY(1086,"天数不能位空"),


    FILE_TYPE_NOT_EMPTY(1087,"请选择文档类型"),
    FILE_FILED_NOT_EMPTY(1088,"文档名称不能为空"),
    FILE_CONTENT_NOT_EMPTY(1089,"文档内容不能为空"),

    FILE_IS_NOT_EXIST(1090,"文档不存在"),

    NOT_AUTHORITY(1091,"无权限操作"),
    EMAIL_NOT_EMPTY(1092,"邮箱不能为空"),
    QQ_NOT_EMPTY(1093,"QQ不能为空"),
    WEIXIN_NOT_EMPTY(1094,"微信不能为空"),


    ;

    private  Integer code;

    private String message;


    HintMessageEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
