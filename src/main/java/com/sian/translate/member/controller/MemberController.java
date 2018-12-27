package com.sian.translate.member.controller;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberService memberService;

    /****
     * 获取会员购买记录
     * @return
     */
    @GetMapping(value = "/getMemberPayRecordList", produces = "application/json;charset=UTF-8")
    ResultVO getMemberPayRecordList(@RequestParam(value = "languageType", required = false) String languageType,
                                    @RequestParam(value = "userId", required = false) Integer userId,
                                    @RequestParam(value = "page", required = false,defaultValue = "1")Integer page,
                                    @RequestParam(value = "size", required = false,defaultValue = "20")Integer size) {
        return  memberService.getInformationList(userId,languageType,page,size);
    }

    /****
     * 获取会员资费列表
     * @return
     */
    @GetMapping(value = "/getMemberConfigList", produces = "application/json;charset=UTF-8")
    ResultVO getMemberConfigList(@RequestParam(value = "languageType", required = false) String languageType) {
        return  memberService.getMemberConfigList(languageType);
    }


}
