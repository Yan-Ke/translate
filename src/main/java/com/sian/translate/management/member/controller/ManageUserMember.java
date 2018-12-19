package com.sian.translate.management.member.controller;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.management.member.service.ManageMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/manage/member")
public class ManageUserMember {


    @Autowired
    ManageMemberService manageMemberService;


    /****
     * 获取会员列表
     * @param isMember 不传默认-1全部 0非会员 1会员
     * @return
     */
    @GetMapping(value = "/getMemberList", produces = "application/json;charset=UTF-8")
    ResultVO getMemberList(@RequestParam(value = "isMember", required = false,defaultValue = "-1") Integer isMember,
                           @RequestParam(value = "page", required = false,defaultValue = "1")Integer page,
                           @RequestParam(value = "size", required = false,defaultValue = "20")Integer size,
                           HttpSession session) {
        return  manageMemberService.getMemberList(isMember,page,size,session);

    }


}
