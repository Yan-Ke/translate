package com.sian.translate.management.coupon.service;

import com.sian.translate.VO.ResultVO;

import javax.servlet.http.HttpSession;

public interface CounponManageService {

    ResultVO getCouponList(String name, Integer page, Integer size, HttpSession session);

    ResultVO addCoupon(String name, Integer type, String fullPrice, String reducePrice, String beginTime, String endTime, Integer day,Integer count, HttpSession session);

    ResultVO editCoupon(Integer id, String name, Integer type, String fullPrice, String reducePrice, String beginTime, String endTime, Integer day,Integer count, HttpSession session);

    ResultVO deleteCoupon(Integer id, HttpSession session);

    ResultVO grantCoupon(Integer id, Integer type, Integer memberType, String memberPhones, HttpSession session);

    ResultVO getCouponRecordList(String name, Integer page, Integer size, HttpSession session);
}
