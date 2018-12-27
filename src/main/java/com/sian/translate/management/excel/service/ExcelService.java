package com.sian.translate.management.excel.service;

import com.sian.translate.VO.ResultVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public interface ExcelService {

    ResultVO exportmemberUserInfo(Integer isMember, HttpServletResponse response, Integer page, Integer size, HttpSession session)throws Exception;

    ResultVO importDictionary(MultipartFile file, HttpSession session)throws IOException;
}
