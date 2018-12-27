package com.sian.translate.pay.service;

import com.alipay.api.AlipayApiException;
import com.sian.translate.VO.ResultVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AlipayService {
    /**
     * 付款
     * @return 付款返回值
     * @param userId
     * @param memberId
     * @param id
     */
    ResultVO createOrder(Integer userId, Integer memberId, Integer id,String languageType,Integer payType) throws Exception;

    /**
     * 付款异步通知调用地址
     * @param request 新增参数
     * @return 新增返回值
     */
    void notify(HttpServletRequest request, HttpServletResponse response,int payType) throws AlipayApiException;

}
