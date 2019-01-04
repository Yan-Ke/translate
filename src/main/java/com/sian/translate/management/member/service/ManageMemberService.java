package com.sian.translate.management.member.service;

import com.sian.translate.VO.ResultVO;

import javax.servlet.http.HttpSession;

public interface ManageMemberService {


    ResultVO getMemberList(Integer isMember,String param, Integer page, Integer size, HttpSession session);

    ResultVO editMemberConfig(Integer id,String explainChinese, String explainZang, String amount, Integer month, HttpSession session);


    ResultVO getAllMemberPayRecordList(String beginTime,String endTime, String orderNo, String nickName, Integer month,Integer page, Integer size, HttpSession session);


    ResultVO addMemberConfig(String explainChinese, String explainZang, String monthAmount, String quarterAmount, String halfYearAmount, String yearAmount, HttpSession session);

    ResultVO getAllFinancialInfo(String beginTime, String endTime, Integer page, Integer size, HttpSession session);
}
