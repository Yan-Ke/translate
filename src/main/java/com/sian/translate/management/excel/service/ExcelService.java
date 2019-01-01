package com.sian.translate.management.excel.service;

import com.sian.translate.VO.ResultVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public interface ExcelService {

    ResultVO exportmemberUserInfo(Integer isMember, HttpServletResponse response, Integer page, Integer size, String param,HttpSession session)throws Exception;

    ResultVO importDictionary(Integer id,MultipartFile file, HttpSession session)throws IOException;

    ResultVO exportThesaurus(Integer id, String name, HttpServletResponse response, Integer page, Integer size, HttpSession session) throws Exception;

    ResultVO exportMemberPayRecord(String beginTime, String endTime, String orderNo, String nickName, Integer page, Integer size, HttpServletResponse response, HttpSession session) throws Exception;

    ResultVO exportFinancialInfo(String beginTime, String endTime, Integer page, Integer size, HttpServletResponse response, HttpSession session) throws Exception;
}
