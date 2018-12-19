package com.sian.translate.information.controller;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.information.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/information")
public class InformationController {

    @Autowired
    InformationService informationService;

    /****
     * 获取资讯列表
     * @param languageType
     * @return
     */
    @GetMapping(value = "/getInformationList", produces = "application/json;charset=UTF-8")
    ResultVO getInformationList(@RequestParam(value = "languageType", required = false) String languageType
    ,@RequestParam(value = "page", required = false,defaultValue = "1")Integer page,@RequestParam(value = "size", required = false,defaultValue = "20")Integer size) {
        return  informationService.getInformationList(languageType,page,size);
    }

}
