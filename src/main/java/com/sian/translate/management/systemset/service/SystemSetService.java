package com.sian.translate.management.systemset.service;

import com.sian.translate.VO.ResultVO;

import javax.servlet.http.HttpSession;

public interface SystemSetService {



    ResultVO getFeedbackList(Integer status, String nickName, String content, Integer page, Integer size, HttpSession session);

    ResultVO handleFeedback(Integer id, HttpSession session);

    ResultVO addNotify(Integer languageType, String title, String content, HttpSession session);

    ResultVO deleteNotify(Integer id, HttpSession session);

    ResultVO getNotifykList(String title, Integer page, Integer size, HttpSession session);

    ResultVO getHelpCenterList(String title, Integer page, Integer size, HttpSession session);

    ResultVO addHelpCenter(String title, String content, Integer status,HttpSession session);

    ResultVO deleteHelpCenter(Integer id, HttpSession session);

    ResultVO addRegstieProtocol(String content, HttpSession session);

    ResultVO getRegstieProtocol(HttpSession session);

    ResultVO addAboutMe(String phone, String content, HttpSession session);

    ResultVO getAboutMe(HttpSession session);

    ResultVO getManageLogList(Integer page, Integer size, HttpSession session);
}
