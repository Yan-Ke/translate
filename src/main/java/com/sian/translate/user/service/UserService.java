package com.sian.translate.user.service;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.user.entity.UserInfo;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    /**注册**/
    ResultVO registe(MultipartFile multipartFile,UserInfo userInfo,String type);

    /**登陆**/
    ResultVO login(String phone,String password,String type);

    /**获取用户信息**/
    ResultVO getUserinfo(Integer id,String type);

    /**获取学历列表**/
    ResultVO getDucation(String languageType);

    /**用户帮助反馈**/
    ResultVO feedback(String languageType, Integer userId, String content);
}
