package com.sian.translate.pay.controller;


import com.alipay.api.AlipayApiException;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.pay.service.AlipayService;
import com.sian.translate.utlis.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/***
 * 支付controller
 */
@RestController
@RequestMapping(value = "/pay")
public class PayController {


    @Autowired
    AlipayService alipayService;

    /**
     *
     * @param userId 用户id
     * @param memberId 购买会员对应的id
     * @param couponId 优惠卷id
     * @param type 1支付宝 2微信
     * 调用支付宝为运单付款的Controller

     * @return 支付宝生成的订单信息
     */
    @RequestMapping(value = "/createOrder",produces = "application/json;charset=UTF-8")
    public ResultVO pay(@RequestParam(value = "userId",required = false)Integer userId,
                        @RequestParam(value = "memberId",required = false)Integer memberId,
                        @RequestParam(value = "couponId",required = false) Integer couponId,
                        @RequestParam(value = "payType",required = false,defaultValue = "1") Integer payType,
                        @RequestParam(value = "languageType", required = false) String languageType) {

        try {
            return alipayService.createOrder(userId, memberId,couponId,languageType,payType);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(e.toString());
        }

    }



    /**
     * 订单付款支付宝 异步通知付款状态的Controller
     *
             * @param request
     * @param response
     * @return
             */
    @PostMapping(value = "/aliNotify", produces = "application/json;charset=UTF-8")
    public void aliNotify(HttpServletRequest request,
                       HttpServletResponse response) throws AlipayApiException {
        alipayService.notify(request, response,1);
    }

    /**
     * 订单支付异步通知
     */
    @RequestMapping(value = "/weixinNotify",method = {RequestMethod.GET, RequestMethod.POST})
    public void WXPayBack(HttpServletRequest request,HttpServletResponse response) throws AlipayApiException{
        alipayService.notify(request, response,2);
    }

}
