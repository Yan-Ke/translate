package com.sian.translate.management.statistical.service.impl;

import com.sian.translate.DTO.DayCountDTO;
import com.sian.translate.DTO.StatisticalCountDTO;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.dictionary.repository.ThesaurusRepository;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.information.repository.InformationRepository;
import com.sian.translate.management.statistical.service.StatisticalService;
import com.sian.translate.management.user.service.ManageUserService;
import com.sian.translate.member.repository.MemberPayRecordRepository;
import com.sian.translate.user.repository.UserFeedbackRepositpory;
import com.sian.translate.user.repository.UserInfoRepository;
import com.sian.translate.utlis.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StatisticalServiceImpl implements StatisticalService {

    @Autowired
    HintMessageService hintMessageService;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    MemberPayRecordRepository memberPayRecordRepository;

    @Autowired
    ThesaurusRepository thesaurusRepository;

    @Autowired
    InformationRepository informationRepository;

    @Autowired
    UserFeedbackRepositpory userFeedbackRepositpory;


    @Override
    public ResultVO getTotalStatistical(HttpSession session) {

        String languageType = "0";


        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        /**用户总数**/
        long totalUserCount = userInfoRepository.count();

        /**支付成功的订单总数**/
        long totalOrderCount = memberPayRecordRepository.countByStatus(1);

        /**词条总条数**/
        long totalThesaurusCount = thesaurusRepository.count();

        /**资讯总条数**/
        long totalInformationCount = informationRepository.count();


        /**今日用户新增数**/
        long todayUserCount = userInfoRepository.getTodayCount();

        /**今日订单新增数**/
        long todayOrderCount = memberPayRecordRepository.getTodayCount();


        /**今日订单新增金额**/
        BigDecimal todayOrderAmount = memberPayRecordRepository.getTodayAmount();

        if (todayOrderAmount == null){
            todayOrderAmount = new BigDecimal(0.0) ;
        }

        /**待处理反馈条数**/
        long feeedBackCount = userFeedbackRepositpory.countByStatus(0);

        StatisticalCountDTO statisticalCountDTO =  new StatisticalCountDTO(totalUserCount, totalOrderCount, totalThesaurusCount, totalInformationCount, todayUserCount, todayOrderCount, todayOrderAmount, feeedBackCount);


        return ResultVOUtil.success(statisticalCountDTO);
    }

    @Override
    public ResultVO getOrderStatistical(Integer type,HttpSession session) {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        if (type == null ){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.DAY_NOT_EMPTY.getCode(), languageType));
        }

        List<Object[]> objects;
        if (type == 1){
            objects =  memberPayRecordRepository.getSevenDayOrderCount();
        }else{
            objects = memberPayRecordRepository.getMonthOrderCount();
        }

        List<DayCountDTO> dayCountDTOS = new ArrayList<>();

        setDayCountDTO(objects, dayCountDTOS);


        return ResultVOUtil.success(dayCountDTOS);
    }

    @Override
    public ResultVO geUserStatistical(Integer type, HttpSession session) {
        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        if (type == null ){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.DAY_NOT_EMPTY.getCode(), languageType));
        }

        List<Object[]> objects;
        if (type == 1){
            objects =  userInfoRepository.getSevenDayUserCount();
        }else{
            objects = userInfoRepository.getMonthUserCount();
        }

        List<DayCountDTO> dayCountDTOS = new ArrayList<>();

        setDayCountDTO(objects, dayCountDTOS);


        return ResultVOUtil.success(dayCountDTOS);    }

    private void setDayCountDTO(List<Object[]> objects, List<DayCountDTO> dayCountDTOS) {
        for (Object[] object : objects) {
            DayCountDTO dayCountDTO = new DayCountDTO();
            dayCountDTO.setDate((Date) object[0]);
            BigInteger count = (BigInteger) object[1];
            dayCountDTO.setCount(count.longValue());
            dayCountDTOS.add(dayCountDTO);
        }
    }
}
