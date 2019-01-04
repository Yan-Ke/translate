package com.sian.translate.management.statistical.service;

import com.sian.translate.VO.ResultVO;

import javax.servlet.http.HttpSession;

public interface StatisticalService {


    ResultVO getTotalStatistical(HttpSession session);

    ResultVO getOrderStatistical(Integer type, HttpSession session);

    ResultVO geUserStatistical(Integer type, HttpSession session);

}
