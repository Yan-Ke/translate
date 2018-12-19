package com.sian.translate.user.controller;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.user.entity.UserInfo;
import com.sian.translate.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    UserService userService;

    /***
     * 注册
     * @param nickName 昵称
     * @param password 密码
     * @param openid 微信或qq唯一id
     * @param phone 电话
     * @param sex 性别 1男 2女
     * @param age 年龄
     * @param education 学历
     * @param languageType 语言 0 汉语 1 藏语
     * @param file 头像
     * @return
     */
    @PostMapping("/registe")
    public ResultVO registe(@RequestParam(value = "nickName", required = false) String nickName,
                            @RequestParam(value = "password", required = false) String password,
                            @RequestParam(value = "openid", required = false) String openid,
                            @RequestParam(value = "phone", required = false) String phone,
                            @RequestParam(value = "sex", required = false) Integer sex,
                            @RequestParam(value = "age", required = false) Integer age,
                            @RequestParam(value = "education", required = false) String education,
                            @RequestParam(value = "languageType", required = false) String languageType,
                            @RequestParam(value = "image", required = false) MultipartFile file) {

        UserInfo userInfo = new UserInfo();
        userInfo.setNickName(nickName);
        userInfo.setPassword(password);
        userInfo.setOpenid(openid);
        userInfo.setPhone(phone);
        userInfo.setSex(sex);
        userInfo.setAge(age);
        userInfo.setEducation(education);

        return userService.registe(file, userInfo, languageType);
    }

    /****
     * 登陆
     * @param phone
     * @param password
     * @param languageType
     * @return
     */
    @GetMapping(value = "/login", produces = "application/json;charset=UTF-8")
    ResultVO login(@RequestParam(value = "phone", required = false) String phone, @RequestParam(value = "password", required = false) String password, @RequestParam(value = "languageType", required = false) String languageType) {
        ResultVO resultVO = userService.login(phone, password, languageType);
        return resultVO;
    }

    /*****
     * 通过用户id获取用户信息
     * @param id
     * @param languageType
     * @return
     */
    @GetMapping(value = "/getInfo", produces = "application/json;charset=UTF-8")
    ResultVO getUserInfo(@RequestParam(value = "id", required = false) Integer id, @RequestParam(value = "languageType", required = false) String languageType) {
        ResultVO resultVO = userService.getUserinfo(id, languageType);
        return resultVO;
    }

    /****
     * 获取学历列表
     * @param languageType
     * @return
     */
    @GetMapping(value = "/getDucation", produces = "application/json;charset=UTF-8")
    ResultVO getDucation(@RequestParam(value = "languageType", required = false) String languageType) {
        ResultVO resultVO = userService.getDucation(languageType);
        return resultVO;
    }

    /****
     * 用户帮助反馈
     * @return
     */
    @PostMapping(value = "/feedback", produces = "application/json;charset=UTF-8")
    ResultVO feedback(@RequestParam(value = "languageType", required = false) String languageType,
                      @RequestParam(value = "userId", required = false) Integer userId,
                      @RequestParam(value = "content", required = false) String content) {
        ResultVO resultVO = userService.feedback(languageType,userId,content);
        return resultVO;
    }

}
