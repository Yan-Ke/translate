package com.sian.translate.coupon.controller;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.coupon.service.CouponService;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.utlis.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    CouponService couponService;

    @Autowired
    HintMessageService hintMessageService;

    /****
     * 获取优惠券列表
     * @param languageType 语言
     * @param userId  当前用户id
     * @param status 不传默认所有 0 未使用 1 已经使用 2 已过期
     * @return
     */
    @GetMapping(value = "/getCouponList", produces = "application/json;charset=UTF-8")
    ResultVO getCouponList(@RequestParam(value = "languageType", required = false) String languageType,
                                @RequestParam(value = "userId", required = false, defaultValue = "-1") Integer userId,
                                @RequestParam(value = "status", required = false) String status) {
        return couponService.getCouponList(languageType, userId, status);
    }

    /****
     * 获取待领取优惠券列表
     * @param languageType 语言
     * @param userId  当前用户id
     * @return
     */
    @GetMapping(value = "/getWaitReceiveCouponList", produces = "application/json;charset=UTF-8")
    ResultVO getWaitReceiveCouponList(@RequestParam(value = "languageType", required = false) String languageType,
                           @RequestParam(value = "userId", required = false, defaultValue = "-1") Integer userId) {
        return couponService.getWaitReceiveCouponList(languageType, userId);
    }

    /****
     * 领取优惠券
     * @param languageType 语言
     * @param userId  当前用户id
     * @param id 用户与优惠券中间表id
     * @return
     */
    @PostMapping(value = "/receiveCoupon", produces = "application/json;charset=UTF-8")
    ResultVO receiveCoupon(@RequestParam(value = "languageType", required = false) String languageType,
                           @RequestParam(value = "userId", required = false, defaultValue = "-1") Integer userId,
                           @RequestParam(value = "id", required = false) Integer id)  {
        try {
            return couponService.receiveCoupon(languageType, userId,id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_COUNT_NOT_ENOUGH.getCode(), languageType));
        }
    }


    /****
     * 支付时获取可以使用的优惠券列表
     * @param languageType 语言
     * @param userId  当前用户id
     * @param amount 支付金额
     * @return
     */
    @GetMapping(value = "/findPayCouponList", produces = "application/json;charset=UTF-8")
    ResultVO findPayCouponList(@RequestParam(value = "languageType", required = false) String languageType,
                           @RequestParam(value = "userId", required = false, defaultValue = "-1") Integer userId,
                           @RequestParam(value = "amount", required = false) String amount)  {

        return couponService.findPayCouponList(languageType,userId,amount);
    }

}
