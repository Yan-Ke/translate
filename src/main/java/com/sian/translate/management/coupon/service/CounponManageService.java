package com.sian.translate.management.coupon.service;

import com.sian.translate.VO.ResultVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

public interface CounponManageService {

    ResultVO addCoupon(String name, String content, Integer type, String fullPrice, String reducePrice, String endTime, MultipartFile file, HttpSession session);
}
