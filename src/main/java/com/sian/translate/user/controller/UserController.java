package com.sian.translate.user.controller;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.user.entity.UserInfo;
import com.sian.translate.user.service.UserService;
import com.sian.translate.utlis.ResultVOUtil;
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
     * 第三方登陆
     * @param languageType 语言 0 汉语 1 藏语
     * @return
     */
    @PostMapping("/thridLogin")
    public ResultVO thridLogin(@RequestParam(value = "weixinOpenid", required = false) String weixinOpenid,
                                       @RequestParam(value = "qqOpenid", required = false) String qqOpenid,
                                       @RequestParam(value = "languageType", required = false) String languageType) {

        return userService.thridLogin(weixinOpenid, qqOpenid, languageType);
    }


    /***
     * 第三方登陆后注册
     * @param nickName 昵称
     * @param phone 电话
     * @param sex 性别 1男 2女
     * @param languageType 语言 0 汉语 1 藏语
     * @return
     */
    @PostMapping("/thridLoginRegister")
    public ResultVO thridLoginRegister(@RequestParam(value = "phone", required = false) String phone,
                               @RequestParam(value = "code", required = false) String code,
                               @RequestParam(value = "nickName", required = false) String nickName,
                               @RequestParam(value = "weixinOpenid", required = false) String weixinOpenid,
                               @RequestParam(value = "qqOpenid", required = false) String qqOpenid,
                               @RequestParam(value = "sex", required = false) Integer sex,
                               @RequestParam(value = "imagePath", required = false) String imagePath,
                               @RequestParam(value = "languageType", required = false) String languageType) {

        UserInfo userInfo = new UserInfo();
        userInfo.setNickName(nickName);
        userInfo.setQqOpenid(qqOpenid);
        userInfo.setWeixinOpenid(weixinOpenid);
        userInfo.setPhone(phone);
        userInfo.setSex(sex);
        userInfo.setHeadSmallImage(imagePath);
        userInfo.setHeadBigImage(imagePath);
        userInfo.setOrignalImage(imagePath);


        return userService.thridLoginRegister(code, userInfo, languageType);
    }



    /****
     * 登陆
     * @param phone 手机
     * @param code 验证码
     * @param languageType
     * @return
     */
    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    ResultVO login(@RequestParam(value = "phone", required = false) String phone,
                   @RequestParam(value = "code", required = false) String code,
                   @RequestParam(value = "languageType", required = false) String languageType) {

        //1.直接登陆
        ResultVO resultVO = userService.login(phone, code, languageType);
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
    /***
     * 修改用户信息
     * @param nickName 昵称
     * @param sex 性别 1男 2女
     * @param age 年龄
     * @param educationId 学历ID
     * @param languageType 语言 0 汉语 1 藏语
     * @param file 头像
     * @return
     */
    @PostMapping("/editUserInfo")
    public ResultVO editUserInfo(@RequestParam(value = "id", required = false) Integer id,
                                 @RequestParam(value = "nickName", required = false) String nickName,
                                 @RequestParam(value = "sex", required = false) Integer sex,
                                 @RequestParam(value = "age", required = false) Integer age,
                                 @RequestParam(value = "education", required = false) Integer educationId,
                                 @RequestParam(value = "status", required = false,defaultValue = "0") Integer status,
                                 @RequestParam(value = "phone", required = false) String phone,
                                 @RequestParam(value = "languageType", required = false,defaultValue = "0") String languageType,
                                 @RequestParam(value = "image", required = false) MultipartFile file) {

        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setNickName(nickName);
//        userInfo.setPhone(phone);
        userInfo.setSex(sex);
        userInfo.setAge(age);
        userInfo.setPhone(phone);
        userInfo.setUserStatus(status);
        userInfo.setEducationId(educationId);


        return userService.editUserInfo(file, userInfo, languageType);
    }



    /****
     * 用户帮助反馈
     * @return
     */
    @PostMapping(value = "/feedback", produces = "application/json;charset=UTF-8")
    ResultVO feedback(@RequestParam(value = "languageType", required = false) String languageType,
                      @RequestParam(value = "userId", required = false) Integer userId,
                      @RequestParam(value = "content", required = false) String content,
                      @RequestParam(value = "images", required = false) MultipartFile[] files) {
        ResultVO resultVO = userService.feedback(languageType, userId, content,files);
        return resultVO;
    }

    /****
     * 更改用户手机
     * @param id 用户id
     * @param nowPhone 现在手机号码
     * @param newPhone 新手机号码
     * @param code 验证码
     * @param languageType
     * @return
     */
    @PostMapping(value = "/changePhone", produces = "application/json;charset=UTF-8")
    ResultVO changePhone(@RequestParam(value = "id", required = false) Integer id,
                         @RequestParam(value = "nowPhone", required = false) String nowPhone,
                         @RequestParam(value = "newPhone", required = false) String newPhone,
                         @RequestParam(value = "code", required = false) String code,
                         @RequestParam(value = "languageType", required = false) String languageType) {

        return userService.changePhone(id,nowPhone, newPhone,code, languageType);
    }

    /***
     * 获取系统配置
     * @param type 1关于我们 2注册协议
     * @param languageType
     * @return
     */

    @GetMapping(value = "/getConfig", produces = "application/json;charset=UTF-8")
    ResultVO getConfig(@RequestParam(value = "type", required = false,defaultValue = "0") Integer type, @RequestParam(value = "languageType", required = false) String languageType) {
        return userService.getConfig(type,languageType);
    }

    /***
     * 获取验证码
     * @param languageType
     * @return
     */

    @GetMapping(value = "/getCode", produces = "application/json;charset=UTF-8")
    ResultVO getCode(@RequestParam(value = "languageType", required = false) String languageType) {
        //Todo
        return ResultVOUtil.success();
    }


    /****
     * 获取通知列表
     * @return
     */
    @GetMapping(value = "/getNotifykList", produces = "application/json;charset=UTF-8")
    ResultVO getNotifkList(@RequestParam(value = "languageType", required = false,defaultValue = "0") String languageType,
                            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
        return userService.getNotifkList(languageType, page, size);
    }

    /****
     * 获取帮助中心列表
     * @return
     */
    @GetMapping(value = "/getHelpCenterList", produces = "application/json;charset=UTF-8")
    ResultVO getHelpCenterList(@RequestParam(value = "languageType", required = false,defaultValue = "0") String languageType,
                               @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                               @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
        return userService.getHelpCenterList(languageType, page, size);
    }


}
