package com.sian.translate.coupon.service.impl;

import com.sian.translate.DTO.UserCouponDTO;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.coupon.enity.Coupon;
import com.sian.translate.coupon.enity.UserCouponRecord;
import com.sian.translate.coupon.enity.UserMidCoupon;
import com.sian.translate.coupon.repository.CouponRepository;
import com.sian.translate.coupon.repository.UserCouponRecordRepository;
import com.sian.translate.coupon.repository.UserMidCouponRepository;
import com.sian.translate.coupon.service.CouponService;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.user.entity.UserInfo;
import com.sian.translate.user.repository.UserInfoRepository;
import com.sian.translate.utlis.CommonUtlis;
import com.sian.translate.utlis.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    HintMessageService hintMessageService;

    @Autowired
    UserMidCouponRepository userMidCouponRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    UserCouponRecordRepository userCouponRecordRepository;

    /****
     * 获取优惠券列表
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


        List<UserCouponDTO> couponList;
        if (!StringUtils.isEmpty(status)){
            if (status.equals("0")){
                List<Object[]> list = couponRepository.findUnusedCoupon(userId);
                couponList = CommonUtlis.castEntity(list, UserCouponDTO.class, new UserCouponDTO());
            }else if (status.equals("1")){
                List<Object[]> list = couponRepository.findUsedCoupon(userId);
                couponList = CommonUtlis.castEntity(list, UserCouponDTO.class, new UserCouponDTO());
            }else if (status.equals("2")){
                List<Object[]> list = couponRepository.finDoverdueCoupon(userId);
                couponList = CommonUtlis.castEntity(list, UserCouponDTO.class, new UserCouponDTO());
            } else{
                List<Object[]> list = couponRepository.findAllCoupon(userId);
                couponList = CommonUtlis.castEntity(list, UserCouponDTO.class, new UserCouponDTO());
            }
        }else{
            List<Object[]> list = couponRepository.findAllCoupon(userId);
            couponList = CommonUtlis.castEntity(list, UserCouponDTO.class, new UserCouponDTO());        }




        return ResultVOUtil.success(couponList);
    }

    @Override
    public ResultVO getWaitReceiveCouponList(String languageType, Integer userId) {
        if (userId < 1){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }

        List<Coupon> couponList = couponRepository.findAllWaitReceivceCoupon();


        final Integer id = userId;
        couponList.stream().forEach(coupon -> setCouponOverdueAndReceive(coupon,id));
        List<Coupon> collect = couponList.stream().sorted().collect(Collectors.toList());
        return ResultVOUtil.success(collect);

    }

    @Transactional
    @Override
    public ResultVO receiveCoupon(String languageType, Integer userId, Integer id) throws Exception {
        if (userId < 1){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }

        if (id  == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_ID_NOT_EMPTY.getCode(), languageType));
        }


        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(userId);

        if (!userInfoOptional.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }

        UserInfo userInfo = userInfoOptional.get();
        Optional<Coupon> couponOptional = couponRepository.findById(id);

        if (couponOptional.isPresent()){



            Coupon coupon = couponOptional.get();
            coupon.setCount(coupon.getCount() - 1);
            try {
                couponRepository.save(coupon);
            }catch (Exception e){
                throw new Exception();
            }
            UserMidCoupon userMidCoupon = new UserMidCoupon();
            userMidCoupon.setUserId(userId);
            userMidCoupon.setCouponId(id);
            userMidCoupon.setIsUse(0);
            if (coupon.getType() == 0){
                userMidCoupon.setBeginTime(coupon.getBeginTime());
                userMidCoupon.setEndTime(coupon.getEndTime());
            }else{
                Date nowDate = new Date();
                userMidCoupon.setBeginTime(nowDate);
                userMidCoupon.setEndTime(CommonUtlis.addDay(nowDate, coupon.getDay()));
            }

            UserCouponRecord userCouponRecord = new UserCouponRecord();
            userCouponRecord.setCouponId(id);
            userCouponRecord.setCouponName(coupon.getName());
            if (userInfo.getMemberBeginTime() != null
                    && userInfo.getMemberEndTime() != null) {
                if (CommonUtlis.isEffectiveDate(new Date(), userInfo.getMemberBeginTime(), userInfo.getMemberEndTime())){
                    userCouponRecord.setIsMember(1);
                }else{
                    userCouponRecord.setIsMember(2);
                }
            }else{
                userCouponRecord.setIsMember(0);
            }
            userCouponRecord.setUserHead(userInfo.getHeadBigImage());
            userCouponRecord.setUserName(userInfo.getNickName());
            userCouponRecord.setUserId(userInfo.getId());
            userCouponRecord.setUserPhone(userInfo.getPhone());
            userCouponRecord.setReceiveTime(new Date());

            UserMidCoupon save = userMidCouponRepository.save(userMidCoupon);

            userCouponRecord.setUserMidCouponId(save.getId());
            userCouponRecordRepository.save(userCouponRecord);

            return ResultVOUtil.success();

        }else{
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_NOT_EXIST.getCode(), languageType));
        }

    }

    @Override
    public ResultVO findPayCouponList(String languageType, Integer userId, String amount) {

        if (userId < 1){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }

        BigDecimal amountBigDecimal;
        try {
            amountBigDecimal = new BigDecimal(amount);
        }catch (Exception e){
            e.printStackTrace();
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PAY_FORMAT_ERROR.getCode(), languageType));
        }
        List<Object[]> payCouponList = couponRepository.findPayCouponList(userId, amountBigDecimal);

        List<UserCouponDTO> content = CommonUtlis.castEntity(payCouponList, UserCouponDTO.class, new UserCouponDTO());

        return ResultVOUtil.success(content);
    }

    private void setCouponOverdueAndReceive(Coupon coupon,Integer id){

        coupon.setOverdue(0);
        //截止日期大于当前日期，过期
        if (coupon.getType() == 0){
            if (CommonUtlis.compareNowDate(coupon.getEndTime()) == 1 ){
                coupon.setOverdue(1);
            }
        }
        int count = userMidCouponRepository.countByUserIdAndCouponId(id, coupon.getId());
        if (count > 0){
            coupon.setIsReceive(1);
        }else{
            coupon.setIsReceive(0);
        }

    }

}
