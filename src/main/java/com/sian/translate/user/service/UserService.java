package com.sian.translate.user.service;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.user.entity.UserInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    /**修改用户资料**/
    ResultVO editUserInfo(MultipartFile multipartFile,UserInfo userInfo,String type, HttpServletRequest request);

    /**登陆**/
    ResultVO login(String phone,String code,String type,String deviceId);

    /**获取用户信息**/
    ResultVO getUserinfo(Integer id,String type);

    /**获取学历列表**/
    ResultVO getDucation(String languageType);

    /**用户帮助反馈**/
    ResultVO feedback(String languageType, Integer userId, String content,MultipartFile[] file, HttpServletRequest request);

    /**第三方登陆后注册**/
    ResultVO thridLoginRegister(String code, UserInfo userInfo, String languageType);

    /**第三方登陆**/
    ResultVO thridLogin(String weixinOpenid, String qqOpenid, String languageType,String deviceId);

    /**更改用户手机号码**/
    ResultVO changePhone(Integer id, String nowPhone, String newPhone, String code, String languageType);

    /**获取系统配置**/
    ResultVO getConfig(Integer type, String languageType);

    /**获取通知列表**/
    ResultVO getNotifkList(String languageType, Integer page, Integer size);

    /**获取帮助中心列表**/
    ResultVO getHelpCenterList(String languageType, Integer page, Integer size);

    /**客户端轮询检测用户是其他地方登陆**/
    ResultVO checkLogin(String languageType, Integer userId, String deviceId);
}
