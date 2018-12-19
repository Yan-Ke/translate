package com.sian.translate.coupon.service;

import com.sian.translate.VO.ResultVO;

public interface CouponService {


    /****
     * 获取资讯列表
     * @param languageType 语言
     * @param userId  当前用户id
     * @param status 不传默认所有 0 未使用 1 已经使用 2 已过期
     * @return
     */
    ResultVO getCouponList(String languageType,Integer userId,String status);

}
