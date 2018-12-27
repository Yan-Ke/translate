package com.sian.translate.pay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.StringUtils;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.coupon.enity.Coupon;
import com.sian.translate.coupon.enity.UserMidCoupon;
import com.sian.translate.coupon.repository.CouponRepository;
import com.sian.translate.coupon.repository.UserMidCouponRepository;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.member.enity.MemberConfig;
import com.sian.translate.member.enity.MemberPayRecord;
import com.sian.translate.member.repository.MemberConfigRepository;
import com.sian.translate.member.repository.MemberPayRecordRepository;
import com.sian.translate.pay.service.AlipayService;
import com.sian.translate.pay.utlis.MD5Util;
import com.sian.translate.pay.utlis.OrderInfoUtil2_0;
import com.sian.translate.pay.utlis.PayUtlis;
import com.sian.translate.pay.utlis.WXMyConfigUtil;
import com.sian.translate.user.entity.UserInfo;
import com.sian.translate.user.repository.UserInfoRepository;
import com.sian.translate.utlis.CommonUtlis;
import com.sian.translate.utlis.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.*;

@Service
@Slf4j
public class AlipayServiceImpl implements AlipayService {

    @Value("${pay.zhifubao.ali_notify_url}")
    private String ali_notify_url;

    @Value("${pay.zhifubao.time_express}")
    private String time_express;

    @Value("${pay.zhifubao.appid}")
    private String appid;

    @Value("${pay.zhifubao.private_key}")
    private String private_key;

    @Value("${pay.zhifubao.ali_public_key}")
    private String ali_public_key;

    @Value("${pay.zhifubao.sign_type}")
    private String sign_type;


    @Value("${pay.weixin.weixin_notify_url}")
    private String weixin_notify_url;

    @Value("${pay.body}")
    private String body;

    @Value("${pay.attach}")
    private String attach;


    @Autowired
    HintMessageService hintMessageService;

    @Autowired
    MemberConfigRepository memberConfigRepository;

    @Autowired
    MemberPayRecordRepository memberPayRecordRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    UserMidCouponRepository userMidCouponRepository;


    @Transactional
    @Override
    public ResultVO createOrder(Integer userId, Integer memberId, Integer id, String type, Integer payType) throws Exception {


        if (userId == null) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), type));
        }
        if (memberId == null) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MEMBER_NOT_EMPTY.getCode(), type));
        }

        Optional<MemberConfig> memberConfigOptional = memberConfigRepository.findById(memberId);

        if (!memberConfigOptional.isPresent())
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MEMBER_NOT_EMPTY.getCode(), type));

        MemberConfig memberConfig = memberConfigOptional.get();

        if (memberConfig.getMonth() == null ||
                memberConfig.getMonth() <= 0 ||
                memberConfig.getAmount() == null ||
                memberConfig.getAmount().compareTo(BigDecimal.ZERO) < 1) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MEMBER_NOT_EMPTY.getCode(), type));
        }

        /**优惠卷抵扣金额**/
        BigDecimal couponAccount = new BigDecimal(0);
        BigDecimal total = memberConfig.getAmount();

        int couponId = 0;
        /***优惠卷信息***/
        if (id != null) {

            Optional<UserMidCoupon> userMidCouponOptional = userMidCouponRepository.findById(id);
            if (!userMidCouponOptional.isPresent()){
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_NOT_EXIST.getCode(), type));
            }
            UserMidCoupon userMidCoupon = userMidCouponOptional.get();

            couponId = userMidCoupon.getCouponId();
            Optional<Coupon> couponOptional = couponRepository.findById(couponId);

            if (!couponOptional.isPresent() || userMidCoupon == null) {
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_NOT_EXIST.getCode(), type));
            }
            Coupon coupon = couponOptional.get();
            //优惠卷信息不全
            if (coupon.getFullPrice() == null || coupon.getReducePrice() == null || coupon.getEndTime() == null ) {
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_INFO_INCOMPLETE.getCode(), type));
            }
            //已经过期
            if (CommonUtlis.compareNowDate(coupon.getEndTime()) == 1) {
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_OVERDUE.getCode(), type));
            }
            //已经使用
            if (userMidCoupon.getIsUse() == 1) {
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_USEED.getCode(), type));
            }
            //未满足优惠卷使用金额
            if (coupon.getFullPrice().compareTo(total) != -1) {
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_UNSATISFIED.getCode(), type));
            }
            //抵扣金额大于了总金额
            if (coupon.getReducePrice().compareTo(total) >= 0) {
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_OVER_TOTAL.getCode(), type));
            }

            couponAccount = coupon.getReducePrice();
        }

        /***会员相关信息***/
        int month = memberConfig.getMonth();
        String subject = month + "个月会员充值";//商品名称
        String order_no = PayUtlis.getOrderId(userId);   //生成商户订单号，不可重复
        String totalFee = total.subtract(couponAccount).toString();

        MemberPayRecord memberPayRecord = new MemberPayRecord();
        memberPayRecord.setOrderId(order_no);
        memberPayRecord.setUserId(userId);
        memberPayRecord.setAmount(new BigDecimal(totalFee));
        memberPayRecord.setMonth(month);
        memberPayRecord.setPayTime(new Date());
        memberPayRecord.setReduceAmount(couponAccount);
        memberPayRecord.setStatus(0);
        memberPayRecord.setPayType(payType);
        if (couponId <= 0) {
            memberPayRecord.setCouponId(couponId+"");
        }
        memberPayRecordRepository.save(memberPayRecord);


        String orderInfo = "";

        /**支付寶支付**/
        if (payType == 1) {
            Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(appid, sign_type, body, order_no, totalFee, time_express, ali_notify_url, subject);
            String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
            String privateKey = private_key;
            String sign = OrderInfoUtil2_0.getSign(params, privateKey, sign_type);
            orderInfo = orderParam + "&" + sign;
        } else {
            // 微信支付钱的单位为分
            String total_fee = total.subtract(couponAccount).divide(new BigDecimal(100), 0, BigDecimal.ROUND_HALF_UP).toString();

            WXMyConfigUtil config = new WXMyConfigUtil();
            Map<String, String> result = dounifiedOrder(order_no, total_fee);
            String nonce_str = (String) result.get("nonce_str");
            String prepay_id = (String) result.get("prepay_id");
            Long time = System.currentTimeMillis() / 1000;
            String timestamp = time.toString();

            //签名生成算法
            Map<String, String> map = new HashMap<>();
            map.put("appid", config.getAppID());
            map.put("partnerid", config.getMchID());
            map.put("package", "Sign=WXPay");
            map.put("noncestr", nonce_str);
            map.put("timestamp", timestamp);
            map.put("prepayid", prepay_id);
            String sign = MD5Util.getSign(map);

            orderInfo = "{\"appid\":\"" + config.getAppID() + "\",\"partnerid\":\"" + config.getMchID() + "\",\"package\":\"Sign=WXPay\"," +
                    "\"noncestr\":\"" + nonce_str + "\",\"timestamp\":" + timestamp + "," +
                    "\"prepayid\":\"" + prepay_id + "\",\"sign\":\"" + sign + "\"}";
        }

        return ResultVOUtil.success(orderInfo);

    }

    /**
     * 支付宝微信 付款异步通知调用地址
     *
     * @param request  新增参数
     * @param response
     * @param payType 1支付宝 2微信
     * @return 新增返回值
     */
    @Transactional
    @Override
    public void notify(HttpServletRequest request, HttpServletResponse response, int payType) throws AlipayApiException {

        boolean flag = false;
        String out_trade_no = "";
        if (payType == 1) {
            Map<String, String> params = new HashMap<String, String>();
            Map requestParams = request.getParameterMap();
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                params.put(name, valueStr);
            }
            System.err.println(params);
            String CHARSET = "UTF-8";

            String tradeStatus = request.getParameter("trade_status");
            out_trade_no = request.getParameter("out_trade_no");//商户订单号
            System.out.println("回调。。。" + out_trade_no);
            boolean aliFlag = AlipaySignature.rsaCheckV1(params, ali_public_key, CHARSET, sign_type);
            if (aliFlag) {
                if (tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS")) {
                    flag = true;
                }
            }


        } else {
            String resXml = "";
            System.err.println("进入异步通知");
            try {
                //
                InputStream is = request.getInputStream();
                //将InputStream转换成String
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = null;
                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                resXml = sb.toString();
                System.err.println(resXml);

                WXMyConfigUtil config = null;
                try {
                    config = new WXMyConfigUtil();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                WXPay wxpay = new WXPay(config);
                Map<String, String> notifyMap = null;
                try {
                    notifyMap = WXPayUtil.xmlToMap(resXml);         // 转换成map
                    if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
                        // 签名正确
                        // 进行处理。
                        // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                        String return_code = notifyMap.get("return_code");//状态
                        out_trade_no = notifyMap.get("out_trade_no");//订单号

                        if (return_code.equals("SUCCESS")) {
                            if (out_trade_no != null) {
                                flag = true;
                                log.info("微信手机支付回调成功订单号:{}", out_trade_no);
                            } else {
                                log.info("微信手机支付回调失败订单号:{}", out_trade_no);
                            }

                        }
                    } else {
                        // 签名错误，如果数据里没有sign字段，也认为是签名错误
                        log.error("手机支付回调通知签名错误");
                    }
                } catch (Exception e) {
                    log.error("手机支付回调通知失败", e);
                }

            } catch (Exception e) {
                log.error("手机支付回调通知失败", e);
                String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
        }

        out_trade_no = "1";
        if (flag) {//验证成功
            if (StringUtils.isEmpty(out_trade_no)) {
                log.error("订单ID为空");
            }
            MemberPayRecord memberPayRecord = memberPayRecordRepository.findByOrderId(out_trade_no);

            if (memberPayRecord == null) {
                log.error("订单={}不存在", out_trade_no);
            } else {

                Integer num = memberPayRecordRepository.updateByOrderId(1, out_trade_no);
                if (num > 0) {
                    log.error("订单={}更新成功", out_trade_no);
                }

                int month = memberPayRecord.getMonth();
                int userId = memberPayRecord.getUserId();
                Optional<UserInfo> userInfoOptional = userInfoRepository.findById(userId);

                if (userInfoOptional.isPresent()) {
                    UserInfo userInfo = userInfoOptional.get();

                    Date memberBeginTime = userInfo.getMemberBeginTime();
                    Date memberEndTime = userInfo.getMemberEndTime();

                    //默认为当前时间
                    Date memberBeginResultTime = new Date();
                    Date memberEndTimeResultTime = CommonUtlis.subMonth(memberBeginResultTime, month);

                    //开通过会员不更改日期
                    if (memberBeginTime != null) {
                        memberBeginResultTime = memberBeginTime;
                        memberEndTimeResultTime = CommonUtlis.subMonth(memberEndTime, month);
                    }
                    //开通过会员已经过期修改会员开始时间为现在
                    if (memberEndTime != null && CommonUtlis.compareNowDate(memberEndTime) == 1) {
                        memberBeginResultTime = new Date();
                        memberEndTimeResultTime = CommonUtlis.subMonth(memberBeginResultTime, month);
                    }
                    userInfoRepository.updateMemberDateById(memberBeginResultTime, memberEndTimeResultTime, userId);

                }
            }
        } else {//验证失败
            Integer num = memberPayRecordRepository.updateByOrderId(2, out_trade_no);
            System.out.println("fail vaild");
        }

    }


    private Map<String, String> dounifiedOrder(String order_no, String total_fee) throws Exception {

        //获取本机IP
        String host = InetAddress.getLocalHost().getHostAddress();

        Map<String, String> fail = new HashMap<>();
        WXMyConfigUtil config = new WXMyConfigUtil();
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<String, String>();
        data.put("body", body);
        data.put("out_trade_no", order_no);
        data.put("total_fee", total_fee);
        data.put("spbill_create_ip", host);
        //异步通知地址（请注意必须是外网）
        data.put("notify_url", weixin_notify_url);
        data.put("trade_type", "APP");
        data.put("attach", attach);
//        data.put("sign", md5Util.getSign(data));
        StringBuffer url = new StringBuffer();
        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            System.out.println(resp);
            String returnCode = resp.get("return_code");    //获取返回码
            String returnMsg = resp.get("return_msg");

            if ("SUCCESS".equals(returnCode)) {       //若返回码为SUCCESS，则会返回一个result_code,再对该result_code进行判断
                String resultCode = (String) resp.get("result_code");
                String errCodeDes = (String) resp.get("err_code_des");
                System.out.print(errCodeDes);
                if ("SUCCESS".equals(resultCode)) {
                    //获取预支付交易回话标志
                    Map<String, String> map = new HashMap<>();
                    String prepay_id = resp.get("prepay_id");
                    String signType = "MD5";
                    map.put("prepay_id", prepay_id);
                    map.put("signType", signType);
                    String sign = MD5Util.getSign(map);
                    resp.put("realsign", sign);
                    url.append("prepay_id=" + prepay_id + "&signType=" + signType + "&sign=" + sign);
                    return resp;
                } else {
                    log.info("订单号：{},错误信息：{}", order_no, errCodeDes);
                    url.append(errCodeDes);
                    throw new RuntimeException(errCodeDes);
                }
            } else {
                log.info("订单号：{},错误信息：{}", order_no, returnMsg);
                url.append(returnMsg);
                throw new RuntimeException(returnMsg);
            }

        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return fail;
    }


}