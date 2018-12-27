package com.sian.translate.management.coupon.controller;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.management.coupon.service.CounponManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

/***
 * 优惠卷管理
 */
@RestController
@RequestMapping(value = "/manage/coupon")
public class CouponManageController {

    @Autowired
    CounponManageService counponManageService;

    /****
     *  新增优惠卷
     * @param name 优惠卷名称
     * @param content 优惠卷内容
     * @param type 0汉语 1藏语
     * @param fullPrice 满足此金额才能使用
     * @param reducePrice 抵扣金额
     * @param endTime 截止使用日期 时间搓
     * @param file 图片
     * @return
     */
    @PostMapping(value = "/addCoupon", produces = "application/json;charset=UTF-8")
    ResultVO addCoupon(@RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "content", required = false) String content,
                       @RequestParam(value = "type", required = false) Integer type,
                       @RequestParam(value = "fullPrice", required = false) String fullPrice,
                       @RequestParam(value = "reducePrice", required = false) String reducePrice,
                       @RequestParam(value = "endTime", required = false) String endTime,
                       @RequestParam(value = "file", required = false) MultipartFile file,
                       HttpSession session) {
        return counponManageService.addCoupon(name, content, type,fullPrice,reducePrice,endTime,file,session);
    }

}
