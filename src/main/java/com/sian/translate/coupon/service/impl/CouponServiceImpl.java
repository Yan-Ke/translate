package com.sian.translate.coupon.service.impl;

import com.sian.translate.DTO.PageInfoDto;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.coupon.enity.Coupon;
import com.sian.translate.coupon.repository.CouponRepository;
import com.sian.translate.coupon.service.CouponService;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.utlis.CommonUtlis;
import com.sian.translate.utlis.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    HintMessageService hintMessageService;

    /****
     * 获取资讯列表
     * @param languageType 语言
     * @param userId  当前用户id
     * @param status 不传默认所有 0 未使用 1 已经使用 2 已过期
     * @return
     */
    @Override
    public ResultVO getCouponList(String languageType, Integer userId, String status) {

        if (userId < 1){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }

        int type = 0;
        if (!StringUtils.isEmpty(languageType) && languageType.equals("1")){
            type = 1;
        }


        List<Coupon> couponList;
        if (!StringUtils.isEmpty(status)){
            if (status.equals("0")){
                couponList = couponRepository.findUnusedCoupon(userId);
            }else if (status.equals("1")){
                couponList = couponRepository.findUsedCoupon(userId);
            }else if (status.equals("2")){
                couponList = couponRepository.finDoverdueCoupon(userId);
            } else{
                couponList = couponRepository.findAllCoupon(userId);
            }
        }else{
            couponList = couponRepository.findAllCoupon(userId);
        }




        int finalType = type;
        couponList.stream().forEach(coupon -> setContentAndName(coupon, finalType));

        return ResultVOUtil.success(couponList);
    }

    private void setContentAndName(Coupon coupon,Integer type){

        coupon.setOverdue(0);
        //截止日期大于当前日期，过期
        if (CommonUtlis.compareNowDate(coupon.getEndTime()) == 1 ){
            coupon.setOverdue(1);
        }

        if (type == 0){
            coupon.setName(coupon.getChineseName());
            coupon.setContent(coupon.getChineseContent());
        }else{
            coupon.setName(coupon.getZangName());
            coupon.setContent(coupon.getZangContent());
        }

    }
}
