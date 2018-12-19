package com.sian.translate.coupon.controller;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.coupon.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    CouponService couponService;

    /****
     * 获取资讯列表
     * @param languageType 语言
     * @param userId  当前用户id
     * @param status 不传默认所有 0 未使用 1 已经使用 2 已过期
     * @return
     */
    @GetMapping(value = "/getCouponList", produces = "application/json;charset=UTF-8")
    ResultVO getCouponList(@RequestParam(value = "languageType", required = false) String languageType,
                                @RequestParam(value = "userId", required = false, defaultValue = "-1") Integer userId,
                                @RequestParam(value = "status", required = false) String status) {
        return couponService.getCouponList(languageType, userId, status);
    }

}
