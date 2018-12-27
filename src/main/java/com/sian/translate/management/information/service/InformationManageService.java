package com.sian.translate.management.information.service;

import com.sian.translate.VO.ResultVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

public interface InformationManageService {


    ResultVO addInformation(String title, String content, String author, String url,Integer languageType,Integer type, Integer order, Integer isShow, MultipartFile file, HttpSession session);

    ResultVO editInformotion(Integer information,String title,String content, String author, String url,Integer languageType,Integer type, Integer order, Integer isShow, MultipartFile file, HttpSession session);

    ResultVO deleteInformotion(Integer informationId, HttpSession session);
}