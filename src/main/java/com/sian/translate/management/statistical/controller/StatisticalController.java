package com.sian.translate.management.statistical.controller;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.management.statistical.service.StatisticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/***
 * 系统首页统计数据
 */
@RestController
@RequestMapping(value = "/manage/statistical")
public class StatisticalController {

    @Autowired
    StatisticalService statisticalService;

    /****
     *  获取统计数据
     * @return
     */
    @GetMapping(value = "/getTotalStatistical", produces = "application/json;charset=UTF-8")
    ResultVO getTotalStatistical(HttpSession session) {
        return statisticalService.getTotalStatistical(session);
    }

    /****
     *  获取订单统计数据
     * @param type 1 7日内数据 2 30日内数据
     * @return
     */
    @GetMapping(value = "/getOrderStatistical", produces = "application/json;charset=UTF-8")
    ResultVO getOrderStatistical(HttpSession session,
                                 @RequestParam(value = "type", required = false) Integer type) {
        return statisticalService.getOrderStatistical(type,session);
    }

    /****
     *  获取用户统计数据
     * @param type 1 7日内数据 2 30日内数据
     * @return
     */
    @GetMapping(value = "/geUserStatistical", produces = "application/json;charset=UTF-8")
    ResultVO geUserStatistical(HttpSession session,
                               @RequestParam(value = "type", required = false) Integer type) {
        return statisticalService.geUserStatistical(type,session);
    }

}
