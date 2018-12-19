package com.sian.translate.management.user.controller;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.management.user.entity.ManageUserInfo;
import com.sian.translate.management.user.service.ManageUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/manage/user")
public class ManageUserController {


    @Autowired
    ManageUserService manageUserService;


    /****
     *  管理员登陆
     * @return
     */
    @GetMapping(value = "/login", produces = "application/json;charset=UTF-8")
    ResultVO login(@RequestParam(value = "account", required = false) String account,
                   @RequestParam(value = "password", required = false) String password,
                   HttpSession session) {
        ResultVO resultVO = manageUserService.login(account, password,session);
        return resultVO;
    }


}
