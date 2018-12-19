package com.sian.translate.user.service.impl;

import com.sian.translate.TranslateApplicationTests;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

@Slf4j
public class UserServiceImplTest extends TranslateApplicationTests {

    @Autowired
    UserService userService;
    @Test
    public void registe() {
    }

    @Test
    public void login() {
        ResultVO login = userService.login("", "", "1");
        log.info("result={}",login);
    }
}