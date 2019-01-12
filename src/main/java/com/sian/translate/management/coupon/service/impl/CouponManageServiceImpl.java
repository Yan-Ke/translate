package com.sian.translate.management.coupon.service.impl;

import com.sian.translate.DTO.PageInfoDTO;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.coupon.enity.Coupon;
import com.sian.translate.coupon.enity.CouponRecord;
import com.sian.translate.coupon.enity.UserCouponRecord;
import com.sian.translate.coupon.enity.UserMidCoupon;
import com.sian.translate.coupon.repository.CouponRecordRepository;
import com.sian.translate.coupon.repository.CouponRepository;
import com.sian.translate.coupon.repository.UserCouponRecordRepository;
import com.sian.translate.coupon.repository.UserMidCouponRepository;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.management.coupon.service.CounponManageService;
import com.sian.translate.management.user.service.ManageUserService;
import com.sian.translate.user.entity.UserInfo;
import com.sian.translate.user.repository.UserInfoRepository;
import com.sian.translate.utlis.CommonUtlis;
import com.sian.translate.utlis.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CouponManageServiceImpl implements CounponManageService {

    @Autowired
    HintMessageService hintMessageService;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    CouponRecordRepository couponRecordRepository;

    @Autowired
    UserMidCouponRepository userMidCouponRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    UserCouponRecordRepository userCouponRecordRepository;

    @Override
    public ResultVO getCouponList(String name, Integer page, Integer size, HttpSession session) {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");

        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = (int)couponRepository.count();
        }
        Page<Coupon> datas;

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        if (StringUtils.isEmpty(name)) {
            datas = couponRepository.findAll(pageable);
        } else {
            String tempName = "%" + name + "%";
            datas = couponRepository.findAllByNameLike(tempName, pageable);
        }

        int totalElements = (int) datas.getTotalElements(); //总条数
        int totalPages = datas.getTotalPages(); // 总页数
        List<Coupon> content = datas.getContent(); // 数据列表

        datas.stream().forEach(coupon -> setOverdue(coupon));

        PageInfoDTO pageInfoDto = new PageInfoDTO();
        pageInfoDto.setPage(page);
        pageInfoDto.setSize(size);
        pageInfoDto.setTotalElements(totalElements);
        pageInfoDto.setTotalPages(totalPages);
        pageInfoDto.setList(content);

        return ResultVOUtil.success(pageInfoDto);
    }


    @Override
    public ResultVO addCoupon(String name, Integer type, String fullPrice, String reducePrice, String beginTime, String endTime, Integer day, Integer count, HttpSession session) {
        String languageType = "0";


        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        if (StringUtils.isEmpty(name)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_NAME_NOT_EMPTY.getCode(), languageType));
        }


        if (StringUtils.isEmpty(fullPrice)) {
            fullPrice= "0";
//            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_FULL_PRICE_NOT_EMPTY.getCode(), languageType));
        }
        BigDecimal fullPriceBigDecimal;
        try {
            fullPriceBigDecimal = new BigDecimal(fullPrice);
            if (fullPriceBigDecimal.compareTo(BigDecimal.ZERO)  == -1) {
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_FULL_PRICE_LESS_ZERO.getCode(), languageType));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_FULL_PRICE_FORMAT_ERROR.getCode(), languageType));
        }

        if (StringUtils.isEmpty(reducePrice)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_REDUCE_PRICE_NOT_EMPTY.getCode(), languageType));
        }
        BigDecimal reducePriceBigDecimal;
        try {
            reducePriceBigDecimal = new BigDecimal(reducePrice);
            if (reducePriceBigDecimal.compareTo(BigDecimal.ZERO) < 0) {
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_REDUCE_PRICE_LESS_ZERO.getCode(), languageType));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_REDUCE_PRICE_FORMAT_ERROR.getCode(), languageType));
        }

        if (type == 0 && StringUtils.isEmpty(beginTime)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_BEGIN_TIME_NOT_EMPTY.getCode(), languageType));
        }

        if (type == 0 && StringUtils.isEmpty(endTime)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_END_TIME_NOT_EMPTY.getCode(), languageType));
        }


        Date beginTimeDate = null;
        Date endTimeDate = null;



        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if (type == 0) {
            try {
                beginTimeDate = sdf.parse(beginTime);
                endTimeDate = sdf.parse(endTime);

            } catch (Exception e) {
                e.printStackTrace();
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_TIME_FORMAT_ERROR.getCode(), languageType));
            }
        } else {
            if (day == null) {
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_DAY_NOT_EMPTY.getCode(), languageType));
            }
        }

        if (count == null || count < 1) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_COUNT_ERROR.getCode(), languageType));
        }

        Coupon coupon = new Coupon();
        coupon.setName(name);
        coupon.setFullPrice(fullPriceBigDecimal);
        coupon.setReducePrice(reducePriceBigDecimal);
        coupon.setType(type);
        if (type == 0) {
            coupon.setBeginTime(beginTimeDate);
            coupon.setEndTime(endTimeDate);
        } else {
            coupon.setDay(day);
        }
        coupon.setCreateUser(userId);
        coupon.setCreateTime(new Date());
        coupon.setUpdateUser(userId);
        coupon.setUpdateTime(new Date());
        coupon.setCount(count);
        couponRepository.save(coupon);

        String logmsg = "新增优惠券:"+name;

        return ResultVOUtil.success(coupon,logmsg);
    }

    @Override
    public ResultVO editCoupon(Integer id, String name, Integer type, String fullPrice, String reducePrice, String beginTime, String endTime, Integer day,Integer count, HttpSession session) {
        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }
        if (id == null) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_ID_NOT_EMPTY.getCode(), languageType));
        }

        BigDecimal fullPriceBigDecimal = null;
        BigDecimal reducePriceBigDecimal = null;

        if (!StringUtils.isEmpty(fullPrice)) {
            try {
                fullPriceBigDecimal = new BigDecimal(fullPrice);
                if (fullPriceBigDecimal.compareTo(BigDecimal.ZERO) < 0) {
                    return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_FULL_PRICE_LESS_ZERO.getCode(), languageType));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_FULL_PRICE_FORMAT_ERROR.getCode(), languageType));
            }
        }

        if (!StringUtils.isEmpty(reducePrice)) {
            try {
                reducePriceBigDecimal = new BigDecimal(reducePrice);

                if (reducePriceBigDecimal.compareTo(BigDecimal.ZERO) < 0) {
                    return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_REDUCE_PRICE_LESS_ZERO.getCode(), languageType));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_REDUCE_PRICE_FORMAT_ERROR.getCode(), languageType));
            }
        }


        Date beginTimeDate = null;
        Date endTimeDate = null;
        if (type != null) {
            if (type == 0) {
                try {
                    long begin = Long.valueOf(beginTime);
                    long end = Long.valueOf(endTime);


                    if (beginTime.length() == 10 || beginTime.length() == 13) {
                        if (beginTime.length() == 10) {
                            begin = begin * 1000;
                        }
                    } else {
                        return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_TIME_FORMAT_ERROR.getCode(), languageType));
                    }

                    if (endTime.length() == 10 || endTime.length() == 13) {
                        if (endTime.length() == 10) {
                            end = end * 1000;
                        }
                    } else {
                        return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_TIME_FORMAT_ERROR.getCode(), languageType));
                    }

                    beginTimeDate = new Date(begin);
                    endTimeDate = new Date(end);
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_TIME_FORMAT_ERROR.getCode(), languageType));
                }
            } else {
                if (day == null) {
                    return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_DAY_NOT_EMPTY.getCode(), languageType));
                }
            }
        }


        Optional<Coupon> couponOptional = couponRepository.findById(id);

        if (!couponOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_NOT_EXIST.getCode(), languageType));
        }

        Coupon coupon = couponOptional.get();

        if (!StringUtils.isEmpty(name)) {
            coupon.setName(name);
        }
        if (type != null) {
            coupon.setType(type);
            if (type == 0 && beginTime != null && endTime != null) {
                coupon.setBeginTime(beginTimeDate);
                coupon.setEndTime(endTimeDate);
            }
            if (type == 1 && day != null) {
                coupon.setDay(day);
            }
        }
        if (fullPrice != null) {
            coupon.setFullPrice(fullPriceBigDecimal);
        }

        if (reducePriceBigDecimal != null) {
            coupon.setReducePrice(reducePriceBigDecimal);
        }
        coupon.setUpdateUser(userId);
        coupon.setUpdateTime(new Date());

        if (count != null && count > 0){
            coupon.setCount(count);
        }
        couponRepository.save(coupon);

        String logmsg = "修改优惠券:"+coupon.getName();


        return ResultVOUtil.success(coupon,logmsg);
    }

    @Transactional
    @Override
    public ResultVO deleteCoupon(Integer id, HttpSession session) {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }
        if (id == null) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_ID_NOT_EMPTY.getCode(), languageType));
        }


        Optional<Coupon> byId = couponRepository.findById(id);
        if (!byId.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_NOT_EXIST.getCode(), languageType));
        }

        Coupon coupon = byId.get();

        userMidCouponRepository.deleteByCouponId(id);
        couponRepository.deleteById(id);

        String logmsg = "删除优惠券:"+coupon.getName();

        return ResultVOUtil.success(logmsg);
    }

    @Transactional
    @Override
    public ResultVO grantCoupon(Integer id, Integer type, Integer memberType, String memberPhones, HttpSession session) {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }
        if (id == null) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_IS_NULL  .getCode(), languageType));
        }

        if (type == null || (type != 0 && type != 1 && type != 2)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_GRANT_TYPE_NOT_EMPTY.getCode(), languageType));
        }

        if (type == 0) {
            if (StringUtils.isEmpty(memberPhones)) {
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_GRANT_MEMBERPHONE_NOT_EMPTY.getCode(), languageType));
            }
        }
        if (type == 1) {
            if (memberType == null || (memberType != 0 && memberType != 1)) {
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_GRANT_MEMBER_TYPE_NOT_EMPTY.getCode(), languageType));
            }
        }

        Optional<Coupon> couponOptional = couponRepository.findById(id);

        if (!couponOptional.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.COUPON_NOT_EXIST.getCode(), languageType));
        }
        Coupon coupon = couponOptional.get();

        CouponRecord couponRecord = new CouponRecord();
        couponRecord.setCouponId(id);
        couponRecord.setName(coupon.getName());
        couponRecord.setType(type);
        if (type == 0) {
            couponRecord.setMemberPhones(memberPhones);
        }
        if (type == 1) {
            couponRecord.setMemberType(memberType);
        }
        couponRecord.setLssuerId(userId);
        couponRecord.setGrantTime(new Date());

        couponRecordRepository.save(couponRecord);

        Date receiveTime = new Date();

        if (type == 0) {
            String[] split = memberPhones.split(",");
            if (split.length > 0) {

                List<UserInfo> userInfoList = userInfoRepository.findByPhoneIn(split);
                saveUserCouponRecord(id, coupon, receiveTime, userInfoList);
            }


        } else if (type == 1) {
            List<UserInfo> userInfoList;
            if (memberType == 0) {
                userInfoList = userInfoRepository.findByMemberEndTimeIsNull();
            } else {
                userInfoList = userInfoRepository.findByMemberEndTimeIsNotNull();
            }

            saveUserCouponRecord(id, coupon, receiveTime, userInfoList);
        } else if (type == 2) {
            List<UserInfo> userInfoList = userInfoRepository.findAll();
            saveUserCouponRecord(id, coupon, receiveTime, userInfoList);
        }
        String logmsg = "发放优惠券";


        return ResultVOUtil.success(couponRecord,logmsg);
    }


    @Override
    public ResultVO getCouponRecordList(String name, Integer page, Integer size, HttpSession session) {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }
        Sort sort = new Sort(Sort.Direction.DESC, "receiveTime");

        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = 1;
        }

        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<UserCouponRecord> userCouponRecordPage;
        if (!StringUtils.isEmpty(name)) {
            String tempName=  "%" + name + "%";
            userCouponRecordPage = userCouponRecordRepository.findByCouponNameLike(pageable, tempName);
        } else {
            userCouponRecordPage = userCouponRecordRepository.findAll(pageable);
        }
        int totalElements = (int) userCouponRecordPage.getTotalElements();
        int totalPages = userCouponRecordPage.getTotalPages();

        List<UserCouponRecord> content = userCouponRecordPage.getContent();
        content.stream().forEach(userCouponRecord -> setUserCouponRecordVipAndUseStatus(userCouponRecord));
        PageInfoDTO pageInfoDTO = new PageInfoDTO();
        pageInfoDTO.setPage(page);
        pageInfoDTO.setSize(size);
        pageInfoDTO.setTotalElements(totalElements);
        pageInfoDTO.setTotalPages(totalPages);
        pageInfoDTO.setList(content);

        return ResultVOUtil.success(pageInfoDTO);
    }

    private void setUserCouponRecordVipAndUseStatus(UserCouponRecord userCouponRecord) {

        if (userCouponRecord.getIsMember() != null){
            if (userCouponRecord.getIsMember() == 0){
                userCouponRecord.setVipIcon(CommonUtlis.VIPICON0);
            }else if (userCouponRecord.getIsMember() == 1){
                userCouponRecord.setVipIcon(CommonUtlis.VIPICON1);
            }else if (userCouponRecord.getIsMember() == 1){
                userCouponRecord.setVipIcon(CommonUtlis.VIPICON2);
            }
        }
        if (StringUtils.isEmpty(userCouponRecord.getUseOrderNo())){
            userCouponRecord.setUseStatus("未使用");
        }else {
            userCouponRecord.setUseStatus("已使用");
        }

    }


    private void saveUserCouponRecord(Integer id, Coupon coupon, Date receiveTime, List<UserInfo> userInfoList) {
        for (UserInfo userInfo : userInfoList) {
            UserCouponRecord userCouponRecord = new UserCouponRecord();
            userCouponRecord.setCouponId(id);
            userCouponRecord.setCouponName(coupon.getName());
            userCouponRecord.setIsMember(0);
            if (userInfo.getMemberBeginTime() != null
                    && userInfo.getMemberEndTime() != null
                    && CommonUtlis.isEffectiveDate(new Date(), userInfo.getMemberBeginTime(), userInfo.getMemberEndTime())) {
                userCouponRecord.setIsMember(1);
            }
            userCouponRecord.setUserHead(userInfo.getHeadBigImage());
            userCouponRecord.setUserName(userInfo.getNickName());
            userCouponRecord.setUserId(userInfo.getId());
            userCouponRecord.setUserPhone(userInfo.getPhone());
            userCouponRecord.setReceiveTime(receiveTime);

            UserMidCoupon userMidCoupon = new UserMidCoupon();
            userMidCoupon.setUserId(userInfo.getId());
            userMidCoupon.setIsUse(0);
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

            UserMidCoupon save = userMidCouponRepository.save(userMidCoupon);

            userCouponRecord.setUserMidCouponId(save.getId());

            userCouponRecordRepository.save(userCouponRecord);
        }
    }

    private void setOverdue(Coupon coupon) {
        coupon.setOverdue(0);
        //截止日期大于当前日期，过期
        if (coupon.getType() == 0) {
            if (CommonUtlis.compareNowDate(coupon.getEndTime()) == 1) {
                coupon.setOverdue(1);
            }
        }
    }
}
