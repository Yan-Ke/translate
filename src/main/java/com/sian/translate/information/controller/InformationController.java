package com.sian.translate.information.controller;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.information.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    ResultVO getInformationList(@RequestParam(value = "userId", required = false) Integer userId,
                                @RequestParam(value = "languageType", required = false) String languageType,
                                @RequestParam(value = "type", required = false,defaultValue = "0") Integer type,
                                @RequestParam(value = "advLocation", required = false,defaultValue = "0") Integer advLocation,
                                @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
        return informationService.getInformationList(userId, languageType, type,advLocation,page, size);
    }

    /****
     * 阅读资讯
     * @param languageType
     * @return
     */
    @PostMapping(value = "/readInformation", produces = "application/json;charset=UTF-8")
    ResultVO readInformation(@RequestParam(value = "languageType", required = false) String languageType, @RequestParam(value = "informationId", required = false) Integer informationId, @RequestParam(value = "userId", required = false) Integer userId) {
        return informationService.readInformation(languageType, informationId, userId);
    }

    /****
     * 赞资讯
     * @param languageType
     * @param type 0 取消赞 1赞
     * @return
     */
    @PostMapping(value = "/zanInformation", produces = "application/json;charset=UTF-8")
    ResultVO zanInformation(@RequestParam(value = "languageType", required = false) String languageType,
                            @RequestParam(value = "informationId", required = false) Integer informationId,
                            @RequestParam(value = "type", required = false) Integer type,
                            @RequestParam(value = "userId", required = false) Integer userId) {


        return informationService.zanInformation(languageType, informationId, type, userId);
    }


}
