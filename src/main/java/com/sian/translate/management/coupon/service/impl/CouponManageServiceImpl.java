package com.sian.translate.management.coupon.service.impl;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.coupon.enity.Coupon;
import com.sian.translate.coupon.repository.CouponRepository;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.management.coupon.service.CounponManageService;
import com.sian.translate.management.user.service.ManageUserService;
import com.sian.translate.utlis.ImageUtlis;
import com.sian.translate.utlis.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class CouponManageServiceImpl implements CounponManageService {

    @Autowired
    HintMessageService hintMessageService;


    @Autowired
    CouponRepository couponRepository;



    @Override
    public ResultVO addCoupon(String name, String content, Integer type, String fullPrice, String reducePrice, String endTime, MultipartFile file, HttpSession session) {
        String languageType = "0";


        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        if (StringUtils.isEmpty(name)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_NAME_NOT_EMPTY.getCode(), languageType));
        }
        if (StringUtils.isEmpty(content)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_CONTENT_NOT_EMPTY.getCode(), languageType));
        }

        if (type == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_LANGUAGETYPE_NOT_EMPTY.getCode(), languageType));
        }
        if (StringUtils.isEmpty(fullPrice)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_FULL_PRICE_NOT_EMPTY.getCode(), languageType));
        }
        BigDecimal fullPriceBigDecimal;
        try {
            fullPriceBigDecimal = new BigDecimal(fullPrice);
        }catch (Exception e){
            e.printStackTrace();
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_FULL_PRICE_FORMAT_ERROR.getCode(), languageType));
        }

        if (StringUtils.isEmpty(reducePrice)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_REDUCE_PRICE_NOT_EMPTY.getCode(), languageType));
        }
        BigDecimal reducePriceBigDecimal;
        try {
            reducePriceBigDecimal = new BigDecimal(reducePrice);
        }catch (Exception e){
            e.printStackTrace();
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_REDUCE_PRICE_FORMAT_ERROR.getCode(), languageType));
        }

        if (StringUtils.isEmpty(endTime)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_END_TIME_NOT_EMPTY.getCode(), languageType));
        }
        Date endTimeDate;
        try {
            long time = Long.valueOf(endTime);

            if (endTime.length() == 10 || endTime.length() == 13){
                if (endTime.length() == 10){
                    time = time * 1000;
                }
            }else{
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_END_TIME_FORMAT_ERROR.getCode(), languageType));
            }
            endTimeDate = new Date(time);
        }catch (Exception e){
            e.printStackTrace();
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_END_TIME_FORMAT_ERROR.getCode(), languageType));
        }




        if (file == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.IMG_NOT_EMPTY.getCode(), languageType));
        }
        String imagePath = "";
        try {
            imagePath = ImageUtlis.loadImage(file);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.IMG_FORMAT_ERROR.getCode(), languageType));
        }

        Coupon coupon = new Coupon();
        coupon.setName(name);
        coupon.setContent(content);
        coupon.setFullPrice(fullPriceBigDecimal);
        coupon.setReducePrice(reducePriceBigDecimal);
        coupon.setEndTime(endTimeDate);
//        coupon.setType(type);
//        coupon.setStatus(0);
        coupon.setCreateUser(userId);
        coupon.setCreateTime(new Date());
        coupon.setUpdateUser(userId);
        coupon.setUpdateTime(new Date());

        couponRepository.save(coupon);

        return ResultVOUtil.success(coupon);
    }
}
