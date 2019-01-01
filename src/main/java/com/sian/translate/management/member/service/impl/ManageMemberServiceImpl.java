package com.sian.translate.management.member.service.impl;

import com.sian.translate.DTO.FinancialInfoDTO;
import com.sian.translate.DTO.PageInfoDTO;
import com.sian.translate.DTO.UserRecordDTO;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.management.member.service.ManageMemberService;
import com.sian.translate.management.user.entity.ManageUserInfo;
import com.sian.translate.management.user.repository.ManageUserInfoRepository;
import com.sian.translate.management.user.service.ManageUserService;
import com.sian.translate.member.enity.MemberConfig;
import com.sian.translate.member.repository.MemberConfigRepository;
import com.sian.translate.member.repository.MemberPayRecordRepository;
import com.sian.translate.user.entity.UserInfo;
import com.sian.translate.user.repository.UserInfoRepository;
import com.sian.translate.utlis.CommonUtlis;
import com.sian.translate.utlis.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Slf4j
@Service
public class ManageMemberServiceImpl implements ManageMemberService {


    @Autowired
    HintMessageService hintMessageService;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    ManageUserInfoRepository manageUserInfoRepository;

    @Autowired
    MemberConfigRepository memberConfigRepository;

    @Autowired
    MemberPayRecordRepository memberpayRecordRepository;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public ResultVO getMemberList(Integer isMember, String param, Integer page, Integer size, HttpSession session) {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }


        boolean isParam = false;
        if (!StringUtils.isEmpty(param)) {
            param = "%" + param + "%";
            isParam = true;
        }


        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = 1;
        }
        List<UserInfo> content; // 数据列表
        int totalElements; //总条数

        if (isMember == 0) {
            if (isParam) {
                totalElements = (int) userInfoRepository.countAllUnmemberByPhoneOrNickName(param);
                content = userInfoRepository.findAllUnmemberUsersByPhoneOrNickName((page - 1) * size, size, param);
            } else {
                totalElements = (int) userInfoRepository.countAllUnmembers();
                content = userInfoRepository.findAllUnmemberUsers((page - 1) * size, size);
            }
        } else if (isMember == 1) {
            if (isParam) {
                totalElements = (int) userInfoRepository.countAllMembersByPhoneOrNickName(param);
                content = userInfoRepository.findAllMemberUsersByPhoneOrNickName((page - 1) * size, size, param);
            } else {
                totalElements = (int) userInfoRepository.countAllMembers();
                content = userInfoRepository.findAllMemberUsers((page - 1) * size, size);
            }
        } else if (isMember == 2) {
            if (isParam) {
                totalElements = (int) userInfoRepository.countOverdueMembersByPhoneOrNickName(param);
                content = userInfoRepository.findOverdueUnmemberUsersByPhoneOrNickName((page - 1) * size, size, param);
            } else {
                totalElements = (int) userInfoRepository.countOverdueMembers();
                content = userInfoRepository.findOverdueUnmemberUsers((page - 1) * size, size);
            }
        } else {
            if (isParam) {
                totalElements = (int) userInfoRepository.countAllByPhoneOrNickName(param);
                content = userInfoRepository.findAllByPhoneOrNickName((page - 1) * size, size, param);
            } else {
                totalElements = (int) userInfoRepository.count();
                content = userInfoRepository.findAll((page - 1) * size, size);
            }
        }


        int totalPages = totalElements % size == 0 ? totalElements / size : totalElements / size + 1;// 总页数
        content.stream().forEach(user -> setUserIsMember(user));

        PageInfoDTO pageInfoDto = new PageInfoDTO();
        pageInfoDto.setPage(page);
        pageInfoDto.setSize(size);
        pageInfoDto.setTotalElements(totalElements);
        pageInfoDto.setTotalPages(totalPages);
        pageInfoDto.setList(content);

        return ResultVOUtil.success(pageInfoDto);
    }

    @Override
    public ResultVO addMemberConfig(String explainChinese, String explainZang, String monthAmount, String quarterAmount, String halfYearAmount, String yearAmount, HttpSession session) {
        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }
        if (StringUtils.isEmpty(explainChinese)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.EXPLAIN_CHINESE_NOT_EMPTY.getCode(), languageType));
        }
//        if (StringUtils.isEmpty(explainZang)){
//            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.EXPLAIN_ZANG_NOT_EMPTY.getCode(), languageType));
//        }


        if (StringUtils.isEmpty(monthAmount)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MEMBER_MONTY_AMOUNT_NOT_EMPTY.getCode(), languageType));
        }
        if (StringUtils.isEmpty(quarterAmount)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MEMBER_QUART_AMOUNT_NOT_EMPTY.getCode(), languageType));
        }
        if (StringUtils.isEmpty(halfYearAmount)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MEMBER_HALF_YEAR_AMOUNT_NOT_EMPTY.getCode(), languageType));
        }
        if (StringUtils.isEmpty(yearAmount)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MEMBER_YEAR_AMOUNT_NOT_EMPTY.getCode(), languageType));
        }


        BigDecimal monthAmountBigDecimal = null;
        try {
            monthAmountBigDecimal = new BigDecimal(monthAmount);
            monthAmountBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MEMBER_MONTY_AMOUNT_NOT_EMPTY.getCode(), languageType));
        }

        BigDecimal quarterAmountBigDecimal = null;
        try {
            quarterAmountBigDecimal = new BigDecimal(quarterAmount);
            quarterAmountBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MEMBER_QUART_AMOUNT_NOT_EMPTY.getCode(), languageType));
        }


        BigDecimal halfYearAmountBigDecimal = null;
        try {
            halfYearAmountBigDecimal = new BigDecimal(halfYearAmount);
            halfYearAmountBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MEMBER_HALF_YEAR_AMOUNT_NOT_EMPTY.getCode(), languageType));
        }

        BigDecimal yearAmountBigDecimal = null;
        try {
            yearAmountBigDecimal = new BigDecimal(yearAmount);
            yearAmountBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MEMBER_YEAR_AMOUNT_NOT_EMPTY.getCode(), languageType));
        }

        List<MemberConfig> memberConfigList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            MemberConfig memberConfig = new MemberConfig();
            memberConfig.setExplainChinese(explainChinese);
            memberConfig.setExplainZang(explainChinese);
            memberConfig.setExplain(explainChinese);
            memberConfig.setCreateUser(userId);
            memberConfig.setUpdateUser(userId);
            memberConfig.setCreateTime(new Date());
            memberConfig.setUpdateTime(new Date());
            if (i == 0) {
                memberConfig.setMonth(1);
                memberConfig.setAmount(monthAmountBigDecimal);
            } else if (i == 1) {
                memberConfig.setMonth(3);
                memberConfig.setAmount(quarterAmountBigDecimal);
            } else if (i == 2) {
                memberConfig.setMonth(6);
                memberConfig.setAmount(halfYearAmountBigDecimal);
            } else if (i == 3) {
                memberConfig.setMonth(12);
                memberConfig.setAmount(yearAmountBigDecimal);
            }
            memberConfigList.add(memberConfig);
        }

        memberConfigRepository.deleteAll();
        memberConfigRepository.saveAll(memberConfigList);

        return ResultVOUtil.success(memberConfigList);
    }

    @Override
    public ResultVO getAllFinancialInfo(String beginTime, String endTime, Integer page, Integer size, HttpSession session) {
        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }

        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = 1;
        }
        StringBuilder dataSql = new StringBuilder("SELECT pay_time as payTime,count(1) as dayCount,SUM(amount) as dayAmount FROM user_order ");

        StringBuilder countSql = new StringBuilder("SELECT count(DISTINCT pay_time) AS count FROM user_order ");



        Date payBeginDate = null;
        Date payEndDate = null;

        if (!StringUtils.isEmpty(beginTime)){
            try {
                long beginTimelong = Long.valueOf(beginTime);


                if (beginTime.length() == 10 || beginTime.length() == 13) {
                    if (beginTime.length() == 10) {
                        beginTimelong = beginTimelong * 1000;
                    }
                } else {
                    return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PAY_TIME_FORMAT_ERROR.getCode(), languageType));
                }

                payBeginDate = new Date(beginTimelong);
            } catch (Exception e) {
                e.printStackTrace();
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PAY_TIME_FORMAT_ERROR.getCode(), languageType));
            }
        }
        if (!StringUtils.isEmpty(endTime)){
            try {
                long endTimelong = Long.valueOf(endTime);


                if (endTime.length() == 10 || endTime.length() == 13) {
                    if (endTime.length() == 10) {
                        endTimelong = endTimelong * 1000;
                    }
                } else {
                    return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PAY_TIME_FORMAT_ERROR.getCode(), languageType));
                }

                payEndDate = new Date(endTimelong);
            } catch (Exception e) {
                e.printStackTrace();
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PAY_TIME_FORMAT_ERROR.getCode(), languageType));
            }
        }


        //拼接where条件
        StringBuilder whereSql = new StringBuilder(" WHERE status = 1");

        if (payBeginDate != null){
            whereSql.append(" AND pay_time >= :payBeginDate");
        }
        if (payEndDate != null){
            whereSql.append(" AND pay_time <= :payEndDate");
        }


        //组装sql语句
        dataSql.append(whereSql).append(" GROUP BY pay_time ORDER BY pay_time DESC");
        countSql.append(whereSql);

        //创建本地sql查询实例
        Query dataQuery = entityManager.createNativeQuery(dataSql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        //设置参数

        if (payBeginDate != null){
            dataQuery.setParameter("payBeginDate", payBeginDate);
            countQuery.setParameter("payBeginDate", payBeginDate);

        }
        if (payEndDate != null){
            dataQuery.setParameter("payEndDate", payEndDate);
            countQuery.setParameter("payEndDate", payEndDate);
        }

        dataQuery.setFirstResult(page - 1);
        dataQuery.setMaxResults(size);

        BigInteger count = (BigInteger) countQuery.getSingleResult();
        int totalElements = count.intValue();
        int totalPages = totalElements % size == 0 ? totalElements / size : totalElements / size + 1;// 总页数

        List result =  page - 1 <= totalPages  ? dataQuery.getResultList() : Collections.emptyList();


        long totalCount = memberpayRecordRepository.countByStatus(1);

        BigDecimal sumAmount = memberpayRecordRepository.getSumAmountByStatus(1);


        List<FinancialInfoDTO> content = new ArrayList<>();
        for (Object item : result) {
            Object[] objects = (Object[]) item;

            FinancialInfoDTO financialInfoDTO = new FinancialInfoDTO();
            if (objects[0] != null){
                financialInfoDTO.setPayTime((Date) objects[0]);
            }
            if (objects[1] != null){
                BigInteger dayCount = (BigInteger) objects[1];
                financialInfoDTO.setDayCount(dayCount.longValue());
            }
            if (objects[2] != null){
                financialInfoDTO.setDayAmount((BigDecimal) objects[2]);
            }
            financialInfoDTO.setTotalAmount(sumAmount);
            financialInfoDTO.setTotalCount(totalCount);
            content.add(financialInfoDTO);

        }


        PageInfoDTO pageInfoDto = new PageInfoDTO();
        pageInfoDto.setPage(page);
        pageInfoDto.setSize(size);
        pageInfoDto.setTotalElements(totalElements);
        pageInfoDto.setTotalPages(totalPages);
        pageInfoDto.setList(content);

        return ResultVOUtil.success(pageInfoDto);

    }

    @Override
    public ResultVO editMemberConfig(Integer id, String explainChinese, String explainZang, String amount, Integer month, HttpSession session) {
        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }
        if (StringUtils.isEmpty(id)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MEBER_CONIFG_ID_NOT_EMPTY.getCode(), languageType));
        }
        Optional<MemberConfig> memberConfigOptional = memberConfigRepository.findById(id);

        if (!memberConfigOptional.isPresent()) {
            {
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MEBER_CONIFG_IS_NULL.getCode(), languageType));
            }
        }

        MemberConfig memberConfig = memberConfigOptional.get();

        if (!StringUtils.isEmpty(explainChinese)) {
            memberConfig.setExplainChinese(explainChinese);
        }
        if (!StringUtils.isEmpty(explainZang)) {
            memberConfig.setExplainZang(explainZang);
        }

        if (!StringUtils.isEmpty(amount)) {

            BigDecimal amountBigDecimal = null;
            try {
                amountBigDecimal = new BigDecimal(amount);
                amountBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (amountBigDecimal != null) {
                memberConfig.setAmount(amountBigDecimal);
            }

        }

        if (!StringUtils.isEmpty(month)) {
            memberConfig.setMonth(month);
        }

        memberConfig.setUpdateUser(userId);
        memberConfig.setUpdateTime(new Date());

        memberConfigRepository.save(memberConfig);

        return ResultVOUtil.success(memberConfig);
    }

    @Override
    public ResultVO getAllMemberPayRecordList(String beginTime,String endTime, String orderNo, String nickName, Integer page, Integer size, HttpSession session) {
        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }

        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = 1;
        }
        StringBuilder dataSql = new StringBuilder("SELECT uo.id as id,uo.user_id as userId,uo.amount as amount,uo.month as month," +
                "uo.pay_time as payTime,uo.coupon_id as counpnId,uo.reduce_amount as reduceAmount,uo.status as status,uo.order_id " +
                "as orderId,uo.pay_type as payType,ui.nick_name as nickName,ui.head_big_image as headBigImage,ui.member_begin_time as memberBeginTime," +
                "ui.member_end_time as memberEndTime,c.name as couponName from user_order as uo " +
                "LEFT JOIN user_info as ui on ui.id = uo.user_id left join coupon as c on c.id = uo.coupon_id ");

        StringBuilder countSql = new StringBuilder("SELECT count(1) FROM user_order uo LEFT JOIN user_info as ui on ui.id = uo.user_id left join coupon as c on c.id = uo.coupon_id ");



        Date payBeginDate = null;
        Date payEndDate = null;

        if (!StringUtils.isEmpty(beginTime)){
            try {
                long beginTimelong = Long.valueOf(beginTime);


                if (beginTime.length() == 10 || beginTime.length() == 13) {
                    if (beginTime.length() == 10) {
                        beginTimelong = beginTimelong * 1000;
                    }
                } else {
                    return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PAY_TIME_FORMAT_ERROR.getCode(), languageType));
                }

                payBeginDate = new Date(beginTimelong);
            } catch (Exception e) {
                e.printStackTrace();
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PAY_TIME_FORMAT_ERROR.getCode(), languageType));
            }
        }
        if (!StringUtils.isEmpty(endTime)){
            try {
                long endTimelong = Long.valueOf(endTime);


                if (endTime.length() == 10 || endTime.length() == 13) {
                    if (endTime.length() == 10) {
                        endTimelong = endTimelong * 1000;
                    }
                } else {
                    return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PAY_TIME_FORMAT_ERROR.getCode(), languageType));
                }

                payEndDate = new Date(endTimelong);
            } catch (Exception e) {
                e.printStackTrace();
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PAY_TIME_FORMAT_ERROR.getCode(), languageType));
            }
        }


        //拼接where条件
        StringBuilder whereSql = new StringBuilder(" WHERE uo.status = 1");

        if (payBeginDate != null){
            whereSql.append(" AND uo.pay_time >= :payBeginDate");
        }
        if (payEndDate != null){
            whereSql.append(" AND uo.pay_time <= :payEndDate");
        }

        if (!StringUtils.isEmpty(orderNo)){
            whereSql.append(" AND uo.order_id like :orderNo");
        }
        if (!StringUtils.isEmpty(nickName)){
            whereSql.append(" AND ui.nick_name like :nickName");
        }

        //组装sql语句
        dataSql.append(whereSql).append(" order by uo.pay_time desc");
        countSql.append(whereSql);

        //创建本地sql查询实例
        Query dataQuery = entityManager.createNativeQuery(dataSql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        //设置参数

        if (payBeginDate != null){
            dataQuery.setParameter("payBeginDate", payBeginDate);
            countQuery.setParameter("payBeginDate", payBeginDate);

        }
        if (payEndDate != null){
            dataQuery.setParameter("payEndDate", payEndDate);
            countQuery.setParameter("payEndDate", payEndDate);
        }

        if (!StringUtils.isEmpty(orderNo)){
            dataQuery.setParameter("orderNo", "%" + orderNo + "%");
            countQuery.setParameter("orderNo", "%"+ orderNo + "%");
        }
        if (!StringUtils.isEmpty(nickName)){
            dataQuery.setParameter("nickName", "%"+ nickName + "%");
            countQuery.setParameter("nickName", "%" + nickName + "%");
        }
        dataQuery.setFirstResult(page - 1);
        dataQuery.setMaxResults(size);

        BigInteger count = (BigInteger) countQuery.getSingleResult();
        int totalElements = count.intValue();
//        int totalElements = (int) memberpayRecordRepository.count(); //总条数
        int totalPages = totalElements % size == 0 ? totalElements / size : totalElements / size + 1;// 总页数

        List result =  page - 1 <= totalPages  ? dataQuery.getResultList() : Collections.emptyList();

//        List<Object[]> result = memberpayRecordRepository.findAllMemberRecords((page - 1) * size, size);

        List<UserRecordDTO> content = new ArrayList<>();
        for (Object item : result) {
            Object[] objects = (Object[]) item;

            UserRecordDTO userRecordDTO = new UserRecordDTO();
            if (objects[0] != null){
                userRecordDTO.setId((Integer) objects[0]);
            }
            if (objects[1] != null){
                userRecordDTO.setUserId((Integer) objects[1]);
            }
            if (objects[2] != null){
                userRecordDTO.setAmount((BigDecimal) objects[2]);
            }
            if (objects[3] != null){
                userRecordDTO.setMonth((Integer) objects[3]);
            }
            if (objects[4] != null){
                userRecordDTO.setPayTime((Date) objects[4]);
            }
            if (objects[5] != null){
                userRecordDTO.setCouponId((Integer) objects[5]);
            }
            if (objects[6] != null){
                userRecordDTO.setReduceAmount((BigDecimal) objects[6]);
            }
            if (objects[7] != null){
                userRecordDTO.setStatus((Integer) objects[7]);
            }
            if (objects[8] != null){
                userRecordDTO.setOrderId((String) objects[8]);
            }
            if (objects[9] != null){
                userRecordDTO.setPayType((Integer) objects[9]);
            }
            if (objects[10] != null){
                userRecordDTO.setNickName((String) objects[10]);
            }
            if (objects[11] != null){
                userRecordDTO.setHeadBigImage((String) objects[11]);
            }
            if (objects[12] != null){
                userRecordDTO.setMemberBeginTime((Date) objects[12]);
            }
            if (objects[13] != null){
                userRecordDTO.setMemberEndTime((Date) objects[13]);
            }
            if (objects[14] != null){
                userRecordDTO.setCouponName((String) objects[14]);
            }


            if (userRecordDTO.getMemberBeginTime() != null
                    && userRecordDTO.getMemberEndTime() != null) {
                if (CommonUtlis.isEffectiveDate(new Date(), userRecordDTO.getMemberBeginTime(), userRecordDTO.getMemberEndTime())) {
                    userRecordDTO.setIsMember(1);
                } else {
                    userRecordDTO.setIsMember(2);
                }
            } else {
                userRecordDTO.setIsMember(0);
            }

            content.add(userRecordDTO);

        }

//        List<UserRecordDTO> content1 = CommonUtlis.castEntity(result, UserRecordDTO.class, new UserRecordDTO());


        PageInfoDTO pageInfoDto = new PageInfoDTO();
        pageInfoDto.setPage(page);
        pageInfoDto.setSize(size);
        pageInfoDto.setTotalElements(totalElements);
        pageInfoDto.setTotalPages(totalPages);
        pageInfoDto.setList(content);

        return ResultVOUtil.success(pageInfoDto);

    }

    private void setUserIsMember(UserInfo userInfo) {
        if (userInfo.getMemberBeginTime() != null
                && userInfo.getMemberEndTime() != null) {
            if (CommonUtlis.isEffectiveDate(new Date(), userInfo.getMemberBeginTime(), userInfo.getMemberEndTime())) {
                userInfo.setIsMember(1);
            } else {
                userInfo.setIsMember(2);
            }
        } else {
            userInfo.setIsMember(0);
        }
    }
}
