package com.sian.translate.management.information.service;

import com.sian.translate.VO.ResultVO;

import javax.servlet.http.HttpSession;

public interface InformationManageService {

    ResultVO addInformation(String title, String content, String author, String url, Integer languageType, Integer type, Integer order, Integer isShow, String image, Integer lookCount, Integer zan, Integer advLocation, Integer recommend,HttpSession session);


//    ResultVO editInformotion(Integer information,String title,String content, String author, String url,Integer languageType,Integer type, Integer order, Integer isShow, MultipartFile file, HttpSession session,HttpServletRequest request);

    ResultVO deleteInformotion(Integer informationId, HttpSession session);

    ResultVO getInformotionList(String title, Integer page, Integer size, HttpSession session);

    ResultVO editInformotion(Integer informationId, String title, String content, String author, String url, Integer languageType, Integer type, Integer order, Integer isShow, String image, Integer lookCount, Integer zan, Integer advLocation, Integer recommend, HttpSession session);
}
