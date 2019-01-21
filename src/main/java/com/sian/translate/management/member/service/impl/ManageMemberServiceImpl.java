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
import com.sian.translate.user.entity.UserEducation;
import com.sian.translate.user.entity.UserInfo;
import com.sian.translate.user.repository.UserEducationRepository;
import com.sian.translate.user.repository.UserInfoRepository;
import com.sian.translate.utlis.CommonUtlis;
import com.sian.translate.utlis.ImageUtlis;
import com.sian.translate.utlis.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
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

    @Autowired
    UserEducationRepository educationRepository;

    @Autowired
    ManageUserService manageUserService;


    @Override
    public ResultVO getMemberList(Integer isMember, String param,Integer month, Integer page, Integer size, HttpSession session) {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }

        Sort sort = new Sort(Sort.Direction.DESC,"registrationTime");

        Pageable pageable = PageRequest.of(page-1, size, sort);

        String finalParam = param;
        Page<UserInfo> userInfoPage = userInfoRepository.findAll(new Specification<UserInfo>() {
            @Override
            public Predicate toPredicate(Root<UserInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicate = new ArrayList<>();

                if (isMember != null && isMember >= 0) {
                    if (isMember == 0) {
                        predicate.add(criteriaBuilder.isNull(root.get("memberEndTime")));
                    }
                    if (isMember == 1) {
                        predicate.add(criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("memberEndTime"), new Date()));
                    }
                    if (isMember == 2) {
                        predicate.add(criteriaBuilder.isNotNull(root.get("memberEndTime")));
                        predicate.add(criteriaBuilder.lessThan(root.<Date>get("memberEndTime"), new Date()));
                    }
                }

                if (!StringUtils.isEmpty(finalParam)) {

                    Predicate predicate1 = criteriaBuilder.like(root.get("nickName"), "%" + finalParam + "%");
                    predicate1 = criteriaBuilder.or(criteriaBuilder.like(root.get("phone"), "%" + finalParam + "%"), predicate1);

                    predicate.add(predicate1);

                }

                if (month != null && month > 0){
                    predicate.add(criteriaBuilder.equal(root.get("memberMonth"), month));
                }


                return criteriaBuilder.and(predicate.toArray(new Predicate[predicate.size()]));
            }
        }, pageable);


        PageInfoDTO pageInfoDto = new PageInfoDTO();
        pageInfoDto.setPage(page);
        pageInfoDto.setSize(size);
        pageInfoDto.setTotalElements((int) userInfoPage.getTotalElements());
        pageInfoDto.setTotalPages(userInfoPage.getTotalPages());
        pageInfoDto.setList(userInfoPage.getContent());

        return ResultVOUtil.success(pageInfoDto);
    }

    @Override
    public ResultVO addMemberConfig(String explainChinese, String explainZang, String monthAmount, String quarterAmount, String halfYearAmount, String yearAmount, HttpSession session) {
        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }
        if (explainChinese == null || StringUtils.isEmpty(explainChinese.trim())) {
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

        String losmsg = "新增会员配置";

        return ResultVOUtil.success(memberConfigList,losmsg);
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
        StringBuilder dataSql = new StringBuilder("SELECT DATE_FORMAT(pay_time,'%Y-%m-%d') AS payTime,COUNT(1) AS dayCount,SUM(amount) AS dayAmount FROM user_order ");
        StringBuilder countSql = new StringBuilder("SELECT count(DISTINCT( DATE_FORMAT(pay_time,'%Y-%m-%d'))) as count FROM user_order ");
        StringBuilder totalSql = new StringBuilder("SELECT COUNT(1) AS totalCount,SUM(amount) AS totalAmount FROM user_order ");

        if (StringUtils.isEmpty(beginTime) && !StringUtils.isEmpty(endTime)){
            beginTime = endTime;
        }

        if (StringUtils.isEmpty(endTime) && !StringUtils.isEmpty(beginTime)){
            endTime = beginTime;
        }

        Date payBeginDate = null;
        Date payEndDate = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if (!StringUtils.isEmpty(beginTime)){

            try {
                payBeginDate = sdf.parse(beginTime);
            } catch (Exception e) {
                e.printStackTrace();
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PAY_TIME_FORMAT_ERROR.getCode(), languageType));
            }
        }
        if (!StringUtils.isEmpty(endTime)){
            try {
                payEndDate = sdf.parse(endTime);
            } catch (Exception e) {
                e.printStackTrace();
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PAY_TIME_FORMAT_ERROR.getCode(), languageType));
            }
        }


        //拼接where条件
        StringBuilder whereSql = new StringBuilder(" WHERE status = 1");

        if (payBeginDate != null){
            whereSql.append(" AND TO_DAYS(pay_time) >= TO_DAYS(:payBeginDate)");
        }
        if (payEndDate != null){
            whereSql.append(" AND TO_DAYS(pay_time) <= TO_DAYS(:payEndDate)");
        }


        //组装sql语句
        dataSql.append(whereSql).append(" GROUP BY DATE_FORMAT(pay_time,'%Y-%m-%d') ORDER BY DATE_FORMAT(pay_time, '%Y-%m-%d') DESC");
        countSql.append(whereSql);
        totalSql.append(whereSql);



        //创建本地sql查询实例
        Query dataQuery = entityManager.createNativeQuery(dataSql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());
        Query totalQuery = entityManager.createNativeQuery(totalSql.toString());

        //设置参数

        if (payBeginDate != null){
            dataQuery.setParameter("payBeginDate", payBeginDate);
            countQuery.setParameter("payBeginDate", payBeginDate);
            totalQuery.setParameter("payBeginDate", payBeginDate);


        }
        if (payEndDate != null){
            dataQuery.setParameter("payEndDate", payEndDate);
            countQuery.setParameter("payEndDate", payEndDate);
            totalQuery.setParameter("payEndDate", payEndDate);
        }

        List resultList = totalQuery.getResultList();


        long totalCount = 0;
        BigDecimal sumAmount = BigDecimal.ZERO;

        if (resultList.size() > 0){

            Object[] objects = (Object[]) resultList.get(0);

            if (objects[0] != null){
                BigInteger temp = (BigInteger) objects[0];
                totalCount = temp.longValue();
            }
            if (objects[1] != null){
                sumAmount = (BigDecimal) objects[1];
            }
        }

        dataQuery.setFirstResult(page - 1);
        dataQuery.setMaxResults(size);

        BigInteger count = (BigInteger) countQuery.getSingleResult();
        int totalElements = count.intValue();
        int totalPages = totalElements % size == 0 ? totalElements / size : totalElements / size + 1;// 总页数

        List result =  page - 1 <= totalPages  ? dataQuery.getResultList() : Collections.emptyList();




        List<FinancialInfoDTO> content = new ArrayList<>();
        for (Object item : result) {
            Object[] objects = (Object[]) item;

            FinancialInfoDTO financialInfoDTO = new FinancialInfoDTO();
            if (objects[0] != null){
//                financialInfoDTO.setPayTime((Date) objects[0]);
                financialInfoDTO.setPayTimeString((String) objects[0]);
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
    public ResultVO editUserInfo(MultipartFile file, UserInfo userInfo, String languageType,HttpSession session, HttpServletRequest request) {




        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }



        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }




        if (StringUtils.isEmpty(userInfo.getId())) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }

        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(userInfo.getId());
        if (!userInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }
        UserInfo resultUserInfo = userInfoOptional.get();


        if (!StringUtils.isEmpty(userInfo.getNickName())) {
            resultUserInfo.setNickName(userInfo.getNickName());
        }

        if (!StringUtils.isEmpty(userInfo.getPhone())) {
            resultUserInfo.setPhone(userInfo.getPhone());
        }
        resultUserInfo.setUserStatus(userInfo.getUserStatus());

        if (userInfo.getSex() != null) {
            resultUserInfo.setSex(userInfo.getSex());
        }
        if (userInfo.getAge() != null) {
            resultUserInfo.setAge(userInfo.getAge());
        }

        if (!StringUtils.isEmpty(userInfo.getEducationId())) {

            Optional<UserEducation> byId = educationRepository.findById(userInfo.getEducationId());

            if (byId.isPresent()) {
                UserEducation userEducation = byId.get();
                resultUserInfo.setEducationId(userInfo.getEducationId());
                if (languageType.equals("0")) {
                    resultUserInfo.setEducation(userEducation.getEducationName());
                } else {
                    resultUserInfo.setEducation(userEducation.getZangEducationName());
                }
            }

        }

        if (file != null && !file.isEmpty()) {
            try {
                HashMap<String, String> map = ImageUtlis.loadImageAndcompressImg(file,request);
                if (map.isEmpty()) {
                    return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.IMG_FORMAT_ERROR.getCode(), languageType));
                }
                resultUserInfo.setHeadSmallImage(map.get("smallFile"));
                resultUserInfo.setHeadBigImage(map.get("bigFile"));
                resultUserInfo.setOrignalImage(map.get("orignalFile"));

            } catch (IOException e) {
                e.printStackTrace();
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.IMG_FORMAT_ERROR.getCode(), languageType));
            }
        }

        setUserIsMember(resultUserInfo);

        resultUserInfo.setUpdateTime(new Date());
        userInfoRepository.save(resultUserInfo);

        String logmsg = "编辑<"+resultUserInfo.getNickName()+">用户资料";

        return ResultVOUtil.success(resultUserInfo,logmsg);
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

        String losmsg = "编辑会员配置";


        return ResultVOUtil.success(memberConfig,losmsg);
    }

    @Override
    public ResultVO getAllMemberPayRecordList(String beginTime,String endTime, String orderNo, String nickName, Integer month,Integer page, Integer size, HttpSession session) {
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

        StringBuilder totalSql = new StringBuilder("SELECT SUM(amount) AS totalAmoun  from user_order as uo LEFT JOIN user_info as ui on ui.id = uo.user_id left join coupon as c on c.id = uo.coupon_id ");


        if (StringUtils.isEmpty(beginTime) && !StringUtils.isEmpty(endTime)){
            beginTime = endTime;
        }

        if (StringUtils.isEmpty(endTime) && !StringUtils.isEmpty(beginTime)){
            endTime = beginTime;
        }

        Date payBeginDate = null;
        Date payEndDate = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if (!StringUtils.isEmpty(beginTime)){

            try {
                payBeginDate = sdf.parse(beginTime);
            } catch (Exception e) {
                e.printStackTrace();
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PAY_TIME_FORMAT_ERROR.getCode(), languageType));
            }
        }
        if (!StringUtils.isEmpty(endTime)){
            try {
                payEndDate = sdf.parse(endTime);
            } catch (Exception e) {
                e.printStackTrace();
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PAY_TIME_FORMAT_ERROR.getCode(), languageType));
            }
        }


        //拼接where条件
        StringBuilder whereSql = new StringBuilder(" WHERE uo.status = 1");


        if (payBeginDate != null){
            whereSql.append(" AND TO_DAYS(uo.pay_time) >= TO_DAYS(:payBeginDate)");

        }
        if (payEndDate != null){
            whereSql.append(" AND TO_DAYS(uo.pay_time) <= TO_DAYS(:payEndDate)");

        }

        if (!StringUtils.isEmpty(orderNo)){
            whereSql.append(" AND uo.order_id like :orderNo");

        }
        if (!StringUtils.isEmpty(nickName)){
            whereSql.append(" AND ui.nick_name like :nickName");

        }
        if (month != null && month > 0){
            whereSql.append(" AND uo.month = :month");

        }

        //组装sql语句
        dataSql.append(whereSql).append(" order by uo.pay_time desc");
        countSql.append(whereSql);
        totalSql.append(whereSql);

        //创建本地sql查询实例
        Query dataQuery = entityManager.createNativeQuery(dataSql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());
        Query totalQuery = entityManager.createNativeQuery(totalSql.toString());

        //设置参数

        if (payBeginDate != null){
            dataQuery.setParameter("payBeginDate", payBeginDate);
            countQuery.setParameter("payBeginDate", payBeginDate);
            totalQuery.setParameter("payBeginDate", payBeginDate);

        }
        if (payEndDate != null){
            dataQuery.setParameter("payEndDate", payEndDate);
            countQuery.setParameter("payEndDate", payEndDate);
            totalQuery.setParameter("payEndDate", payEndDate);

        }

        if (!StringUtils.isEmpty(orderNo)){
            dataQuery.setParameter("orderNo", "%" + orderNo + "%");
            countQuery.setParameter("orderNo", "%"+ orderNo + "%");
            totalQuery.setParameter("orderNo", "%"+ orderNo + "%");

        }
        if (!StringUtils.isEmpty(nickName)){
            dataQuery.setParameter("nickName", "%"+ nickName + "%");
            countQuery.setParameter("nickName", "%" + nickName + "%");
            totalQuery.setParameter("nickName", "%" + nickName + "%");

        }
        if (month != null && month > 0){
            dataQuery.setParameter("month", month);
            countQuery.setParameter("month", month);
            totalQuery.setParameter("month", month);

        }


        List resultList = totalQuery.getResultList();
        BigDecimal sumAmount = BigDecimal.ZERO;

        if (resultList.size() > 0){
            if (resultList.get(0) != null){
                sumAmount = (BigDecimal) resultList.get(0);
            }
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
            userRecordDTO.setTotalAmount(sumAmount);


            if (userRecordDTO.getMonth() == 1){
                userRecordDTO.setMonthString("月");
            }else  if (userRecordDTO.getMonth() == 3){
                userRecordDTO.setMonthString("季度");
            }else  if (userRecordDTO.getMonth() == 6){
                userRecordDTO.setMonthString("半年");
            }else  if (userRecordDTO.getMonth() == 12){
                userRecordDTO.setMonthString("年");
            }

            if (userRecordDTO.getReduceAmount() != null){
                userRecordDTO.setActualAmount(userRecordDTO.getAmount().add(userRecordDTO.getReduceAmount()));
            }else{
                userRecordDTO.setActualAmount(userRecordDTO.getAmount());
            }

            if (userRecordDTO.getMemberBeginTime() != null
                    && userRecordDTO.getMemberEndTime() != null) {
                if (CommonUtlis.isEffectiveDate(new Date(), userRecordDTO.getMemberBeginTime(), userRecordDTO.getMemberEndTime())) {
                    userRecordDTO.setIsMember(1);
                    userRecordDTO.setVipIcon(CommonUtlis.VIPICON1);
                } else {
                    userRecordDTO.setIsMember(2);
                    userRecordDTO.setVipIcon(CommonUtlis.VIPICON2);
                }
            } else {
                userRecordDTO.setIsMember(0);
                userRecordDTO.setVipIcon(CommonUtlis.VIPICON0);

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
                userInfo.setVipIcon(CommonUtlis.VIPICON1);
            } else {
                userInfo.setVipIcon(CommonUtlis.VIPICON2);
                userInfo.setIsMember(2);
            }
        } else {
            userInfo.setVipIcon(CommonUtlis.VIPICON0);
            userInfo.setIsMember(0);
        }

        /**用户性别 0未知1男2女3保密**/
        if(userInfo.getSex() == null || userInfo.getSex() == 0){
            userInfo.setSexString("未知");
        }else if (userInfo.getSex() == 1){
            userInfo.setSexString("男");
        }else if (userInfo.getSex() == 2){
            userInfo.setSexString("女");
        }else if (userInfo.getSex() == 3){
            userInfo.setSexString("保密");
        }


    }


}
