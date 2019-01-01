package com.sian.translate.management.coupon.controller;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.management.coupon.service.CounponManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * 获取优惠券列表
     * @param name 优惠券名称
     * @return
     */
    @GetMapping(value = "/getCouponList", produces = "application/json;charset=UTF-8")
    ResultVO getCouponList(@RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                           @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                           HttpSession session) {
        return counponManageService.getCouponList(name, page, size, session);
    }


    /****
     *  新增优惠卷
     * @param name 优惠卷名称
     * @param type 0 按开始日期到截止计算优惠券周期 1 按几天计算日期，从领取日开始计算
     * @param fullPrice 满足此金额才能使用
     * @param reducePrice 抵扣金额
     * @param beginTime 开始时间 时间搓
     * @param endTime 截止使用日期 时间搓
     * @param day 几天内有效
     * @return
     */
    @PostMapping(value = "/addCoupon", produces = "application/json;charset=UTF-8")
    ResultVO addCoupon(@RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "type", required = false, defaultValue = "0") Integer type,
                       @RequestParam(value = "fullPrice", required = false) String fullPrice,
                       @RequestParam(value = "reducePrice", required = false) String reducePrice,
                       @RequestParam(value = "beginTime", required = false) String beginTime,
                       @RequestParam(value = "endTime", required = false) String endTime,
                       @RequestParam(value = "day", required = false) Integer day,
                       @RequestParam(value = "count", required = false) Integer count,
                       HttpSession session) {
        return counponManageService.addCoupon(name, type, fullPrice, reducePrice, beginTime, endTime, day, count,session);
    }


    /****
     *  修改优惠卷
     * @param id 优惠券id
     * @param name 优惠卷名称
     * @param type 0 按开始日期到截止计算优惠券周期 1 按几天计算日期，从领取日开始计算
     * @param fullPrice 满足此金额才能使用
     * @param reducePrice 抵扣金额
     * @param beginTime 开始时间 时间搓
     * @param endTime 截止使用日期 时间搓
     * @param day 几天内有效
     * @return
     */
    @PostMapping(value = "/editCoupon", produces = "application/json;charset=UTF-8")
    ResultVO editCoupon(@RequestParam(value = "id", required = false) Integer id,
                        @RequestParam(value = "name", required = false) String name,
                        @RequestParam(value = "type", required = false) Integer type,
                        @RequestParam(value = "fullPrice", required = false) String fullPrice,
                        @RequestParam(value = "reducePrice", required = false) String reducePrice,
                        @RequestParam(value = "beginTime", required = false) String beginTime,
                        @RequestParam(value = "endTime", required = false) String endTime,
                        @RequestParam(value = "day", required = false) Integer day,
                        HttpSession session) {
        return counponManageService.editCoupon(id, name, type, fullPrice, reducePrice, beginTime, endTime, day, session);
    }


    /****
     *  删除优惠卷
     * @return
     */
    @PostMapping(value = "/deleteCoupon", produces = "application/json;charset=UTF-8")
    ResultVO deleteCoupon(@RequestParam(value = "id", required = false) Integer id,
                          HttpSession session) {
        return counponManageService.deleteCoupon(id, session);
    }

    /****
     *  发放优惠卷
     * @param type 0指定会员 1会员类型 2全部
     * @param memberType 会员类型 0 新会员 1老会员
     * @return
     */
    @PostMapping(value = "/grantCoupon", produces = "application/json;charset=UTF-8")
    ResultVO grantCoupon(@RequestParam(value = "id", required = false) Integer id,
                         @RequestParam(value = "type", required = false) Integer type,
                         @RequestParam(value = "memberType", required = false) Integer memberType,
                         @RequestParam(value = "memberPhones", required = false) String memberPhones,
                         HttpSession session) {
        return counponManageService.grantCoupon(id, type, memberType, memberPhones, session);
    }

    /****
     *  获取发放优惠券记录
     * @return
     */
    @GetMapping(value = "/getCouponRecordList", produces = "application/json;charset=UTF-8")
    ResultVO getCouponRecordList(@RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                 @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                                 HttpSession session) {
        return counponManageService.getCouponRecordList(name,page,size, session);
    }

}
