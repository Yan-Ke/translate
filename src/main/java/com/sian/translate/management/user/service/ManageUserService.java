package com.sian.translate.management.user.service;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.management.user.entity.ManageUserInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

public interface ManageUserService {

    String SESSION_KEY = "manage_user";

    /**登陆**/
    ResultVO login(String phone, String password, HttpSession session);

}
