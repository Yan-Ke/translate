package com.sian.translate.management.templates.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/manage")
public class LoginController {

    @RequestMapping("/login")
    public String login(){
        return"html/login/index.html";
//        return"html/test.html";
    }
}
