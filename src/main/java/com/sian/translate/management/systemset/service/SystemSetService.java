package com.sian.translate.management.systemset.service;

import com.sian.translate.VO.ResultVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

public interface SystemSetService {



    ResultVO getFeedbackList(Integer status, String nickName, String content, Integer page, Integer size, HttpSession session);

    ResultVO handleFeedback(Integer id, HttpSession session);

    ResultVO getManageLogList(Integer page, Integer size, HttpSession session);

    ResultVO uploadImage(MultipartFile file);



    ResultVO addFile(Integer type, String languageType, String field, String content, Integer status, Integer order, HttpSession session);

    ResultVO deleteFile(Integer id, HttpSession session);

    ResultVO editFile(Integer id, Integer type, String languageType, String field, String content, Integer status, Integer order, HttpSession session);

    ResultVO getFileList(Integer type, String field, Integer page, Integer size, HttpSession session);

    ResultVO getTranslateList(Integer type, String filed, Integer page, Integer size, HttpSession session);
}
