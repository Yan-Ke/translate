package com.sian.translate.management.member.service;

import com.sian.translate.VO.ResultVO;

import javax.servlet.http.HttpSession;

public interface ManageMemberService {


    ResultVO getMemberList(Integer isMember, Integer page, Integer size, HttpSession session);

    ResultVO addMemberConfig(String explainChinese, String explainZang, String amount, Integer month, HttpSession session);

    ResultVO editMemberConfig(Integer id,String explainChinese, String explainZang, String amount, Integer month, HttpSession session);

    ResultVO getAllMemberPayRecordList(Integer page, Integer size, HttpSession session);
}
