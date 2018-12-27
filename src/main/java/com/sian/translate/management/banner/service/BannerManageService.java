package com.sian.translate.management.banner.service;

import com.sian.translate.VO.ResultVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

public interface BannerManageService {

    ResultVO addBanner(String content, String url, Integer type,MultipartFile file,HttpSession session);

    ResultVO editBanner(Integer bannerId,String content, String url, Integer type, MultipartFile file, HttpSession session);

    ResultVO deleteBanner(Integer bannerId, HttpSession session);
}
