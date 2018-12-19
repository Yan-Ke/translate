package com.sian.translate.banner.controller;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.banner.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    BannerService bannerService;

    /****
     * 获取banner列表
     * @param languageType
     * @return
     */
    @GetMapping(value = "/getBannerList", produces = "application/json;charset=UTF-8")
    ResultVO getBannerList(@RequestParam(value = "languageType", required = false) String languageType) {
        return  bannerService.getBannerList(languageType);
    }

}
