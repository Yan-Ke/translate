package com.sian.translate.user.service.impl;

import com.sian.translate.DTO.PageInfoDTO;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.user.entity.SystemConfig;
import com.sian.translate.user.entity.UserEducation;
import com.sian.translate.user.entity.UserFeedback;
import com.sian.translate.user.entity.UserInfo;
import com.sian.translate.user.repository.SystemConfigRepositpory;
import com.sian.translate.user.repository.UserEducationRepository;
import com.sian.translate.user.repository.UserFeedbackRepositpory;
import com.sian.translate.user.repository.UserInfoRepository;
import com.sian.translate.user.service.UserService;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    HintMessageService hintMessageService;

    @Autowired
    UserEducationRepository educationRepository;

    @Autowired
    UserFeedbackRepositpory userFeedbackRepositpory;

    @Autowired
    SystemConfigRepositpory systemConfigRepositpory;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    static final String USER_DEVICE = "user_device_";


    @Override
    public ResultVO editUserInfo(MultipartFile file, UserInfo userInfo, String type, HttpServletRequest request) {


        if (StringUtils.isEmpty(userInfo.getId())) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), type));
        }

        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(userInfo.getId());
        if (!userInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), type));
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
                if (type.equals("0")) {
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
                    return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.IMG_FORMAT_ERROR.getCode(), type));
                }
                resultUserInfo.setHeadSmallImage(map.get("smallFile"));
                resultUserInfo.setHeadBigImage(map.get("bigFile"));
                resultUserInfo.setOrignalImage(map.get("orignalFile"));

            } catch (IOException e) {
                e.printStackTrace();
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.IMG_FORMAT_ERROR.getCode(), type));
            }
        }

        setUserIsMember(resultUserInfo);

        resultUserInfo.setUpdateTime(new Date());
        userInfoRepository.save(resultUserInfo);

        return ResultVOUtil.success(resultUserInfo);
    }


    /*******
     *
     * @param phone 手机号码
     * @param code 验证码
     * @param type 0中文 1藏文
     * @return
     */
    @Override
    public ResultVO login(String phone, String code, String type,String deviceId) {

        if (StringUtils.isEmpty(phone))
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PHONE_NOT_EMPTY.getCode(), type));
        if (StringUtils.isEmpty(code))
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.CODE_NOT_EMPTY.getCode(), type));

        UserInfo userInfo = userInfoRepository.findByPhone(phone);
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setPhone(phone);
            userInfo.setNickName(phone);
            userInfo.setSex(0);
            userInfo.setAge(0);
            userInfo.setBalance(new BigDecimal(0.00));
            userInfo.setRegistrationTime(new Date());
            userInfo.setUpdateTime(new Date());
            userInfo.setUserStatus(0);
        } else {
            if (userInfo.getUserStatus() != null && userInfo.getUserStatus() == 1) {
                ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_PROHIBIT.getCode(), type));
            }
        }

        setUserIsMember(userInfo);
        userInfo.setDeviceId(deviceId);
        userInfo.setLoginTime(new Date());

        UserInfo save = userInfoRepository.save(userInfo);
        stringRedisTemplate.opsForValue().set(USER_DEVICE+save.getId(), deviceId);

//        userInfo.setPassword(password);
        return ResultVOUtil.success(userInfo);
    }

    @Override
    public ResultVO getUserinfo(Integer id, String type) {

        if (StringUtils.isEmpty(id))
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), type));


        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(id);

        if (!userInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), type));
        }



        UserInfo userInfo = userInfoOptional.get();

        if (!StringUtils.isEmpty(userInfo.getEducationId())){
            Optional<UserEducation> byId = educationRepository.findById(userInfo.getEducationId());

            if (byId.isPresent()){
                UserEducation userEducation = byId.get();
                if (type != null && type.equals("1")){
                    userInfo.setEducation(userEducation.getZangEducationName());
                }else {
                    userInfo.setEducation(userEducation.getEducationName());
                }
            }


        }
        setUserIsMember(userInfo);

        return ResultVOUtil.success(userInfo);
    }

    /**
     * 获取学历列表
     *
     * @param languageType
     **/
    @Override
    public ResultVO getDucation(String languageType) {


        List<UserEducation> all = educationRepository.findAll();

        for (UserEducation userEducation : all) {
            if (!StringUtils.isEmpty(languageType) && languageType.equals("1")) {
                userEducation.setName(userEducation.getZangEducationName());
            } else {
                userEducation.setName(userEducation.getEducationName());
            }
        }

        return ResultVOUtil.success(all);

    }

    /**
     * 用户帮助反馈
     *
     * @param languageType
     * @param userId
     * @param content
     **/
    @Override
    public ResultVO feedback(String languageType, Integer userId, String content, MultipartFile[] files, HttpServletRequest request) {

        if (userId == null) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }
        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(userId);

        if (!userInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }
        if (StringUtils.isEmpty(content) || StringUtils.isEmpty(content.trim())) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.FEED_BACK_NOT_EMPTY.getCode(), languageType));
        }
        if (content.length() > 200) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.FEED_BACK_TO_LONG.getCode(), languageType));
        }


        String images = "";

        if (files != null && files.length > 0) {
            try {

                if (files != null && files.length > 0) {
                    for (int i = 0; i < files.length; i++) {
                        MultipartFile file = files[i];
                        // 保存文件
                        images += ImageUtlis.loadImage(file,request) + ",";

                    }
                }
                if (StringUtils.isEmpty(images)) {
                    return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.IMG_FORMAT_ERROR.getCode(), languageType));
                }

            } catch (IOException e) {
                e.printStackTrace();
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.IMG_FORMAT_ERROR.getCode(), languageType));
            }
        }

        UserInfo userInfo = userInfoOptional.get();

        UserFeedback userFeedback = new UserFeedback();
        userFeedback.setUserId(userInfo.getId());
        userFeedback.setContent(content);
        userFeedback.setCreateTime(new Date());
        userFeedback.setUserHead(userInfo.getHeadBigImage());
        userFeedback.setUserNickName(userInfo.getNickName());
        userFeedback.setUserPhone(userInfo.getPhone());
        userFeedback.setImage(images);
        userFeedback.setStatus(0);

        userFeedbackRepositpory.save(userFeedback);

        if (!StringUtils.isEmpty(images)) {
            String[] split = images.split(",");
            List<String> imageList = new ArrayList<>();

            for (String s : split) {
                imageList.add(s);
            }
            userFeedback.setImages(imageList);
        }

        return ResultVOUtil.success(userFeedback);
    }

    /**
     * 第三方登陆后注册
     *
     * @param code
     * @param userInfo
     * @param languageType
     **/
    @Override
    public ResultVO thridLoginRegister(String code, UserInfo userInfo, String languageType) {


        if (StringUtils.isEmpty(userInfo.getPhone())) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PHONE_NOT_EMPTY.getCode(), languageType));
        }
        if (StringUtils.isEmpty(code)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.CODE_NOT_EMPTY.getCode(), languageType));
        }
        if (StringUtils.isEmpty(userInfo.getQqOpenid()) && StringUtils.isEmpty(userInfo.getWeixinOpenid())) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.OPENID_NOT_EMPTY.getCode(), languageType));
        }


        int count = userInfoRepository.countByPhone(userInfo.getPhone());


        if (!StringUtils.isEmpty(userInfo.getQqOpenid())){
            int qqCount = userInfoRepository.countByQqOpenid(userInfo.getQqOpenid());

            if (qqCount > 0){
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.QQ_IS_BAND.getCode(), languageType));
            }
        }

        if (!StringUtils.isEmpty(userInfo.getWeixinOpenid())){
            int weixinCount = userInfoRepository.countByWeixinOpenid(userInfo.getWeixinOpenid());

            if (weixinCount > 0){
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.WEIXIN_IS_BAND.getCode(), languageType));
            }
        }


        if (count > 0) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PHONE_USEED.getCode(), languageType));
        }

        if (StringUtils.isEmpty(userInfo.getNickName())){
            userInfo.setNickName(userInfo.getPhone());
        }

        if (userInfo.getSex() == null) {
            userInfo.setSex(0);
        }
        if (userInfo.getAge() == null) {
            userInfo.setAge(0);
        }
        userInfo.setRegistrationTime(new Date());
        userInfo.setUpdateTime(new Date());
        userInfo.setBalance(new BigDecimal(0.00));
        userInfo.setIsMember(0);
        userInfo.setUserStatus(0);
        userInfo.setLoginTime(new Date());
        userInfo.setDeviceId(userInfo.getDeviceId());

        UserInfo save = userInfoRepository.save(userInfo);
        stringRedisTemplate.opsForValue().set(USER_DEVICE+save.getId(), userInfo.getDeviceId());

        return ResultVOUtil.success(userInfo);


    }

    /**
     * 第三方登陆
     *
     * @param weixinOpenid
     * @param qqOpenid
     * @param languageType
     **/
    @Override
    public ResultVO thridLogin(String weixinOpenid, String qqOpenid, String languageType,String deviceId) {


        if (!StringUtils.isEmpty(qqOpenid)) {
            UserInfo userInfo = userInfoRepository.findByQqOpenid(qqOpenid);

            if (userInfo != null) {
                setUserIsMember(userInfo);
                userInfo.setLoginTime(new Date());
                userInfoRepository.save(userInfo);
                return ResultVOUtil.success(userInfo);
            }
        }

        if (!StringUtils.isEmpty(weixinOpenid)) {
            UserInfo userInfo = userInfoRepository.findByWeixinOpenid(weixinOpenid);

            if (userInfo != null) {
                if (userInfo.getUserStatus() == 1) {
                    ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_PROHIBIT.getCode(), languageType));
                }
                setUserIsMember(userInfo);
                userInfo.setLoginTime(new Date());
                userInfo.setDeviceId(deviceId);
                UserInfo save = userInfoRepository.save(userInfo);
                stringRedisTemplate.opsForValue().set(USER_DEVICE+save.getId(), deviceId);

                return ResultVOUtil.success(userInfo);
            }
        }

        return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));

    }


    /**
     * 更改用户手机号码
     *
     * @param id
     * @param nowPhone
     * @param newPhone
     * @param code
     * @param languageType
     **/
    @Override
    public ResultVO changePhone(Integer id, String nowPhone, String newPhone, String code, String languageType) {
        if (id == null) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }
        if (StringUtils.isEmpty(nowPhone)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOW_PHONE_IS_NOT_EMPTY.getCode(), languageType));
        }
        if (StringUtils.isEmpty(newPhone)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NEW_PHONE_IS_NOT_EMPTY.getCode(), languageType));
        }
        if (StringUtils.isEmpty(code)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.CODE_NOT_EMPTY.getCode(), languageType));
        }

        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(id);
        if (!userInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }
        int count = userInfoRepository.countByPhone(newPhone);

        if (count > 0) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PHONE_USEED.getCode(), languageType));
        }

        UserInfo userInfo = userInfoOptional.get();

        if (!userInfo.getPhone().trim().equals(nowPhone.trim())) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOW_PHONE_ERROR.getCode(), languageType));
        }
        userInfo.setPhone(newPhone);
        userInfoRepository.save(userInfo);
        setUserIsMember(userInfo);

        return ResultVOUtil.success(userInfo);
    }

    /**
     * 获取系统配置
     *
     * @param type
     * @param languageType
     **/
    @Override
    public ResultVO getConfig(Integer type, String languageType) {

        if (StringUtils.isEmpty(type)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.SYSTEM_CONFIG_TYPE_NOT_EMPPPTY.getCode(), languageType));
        }
        Sort sort = new Sort(Sort.Direction.DESC,"configOrder").and(new Sort(Sort.Direction.DESC, "updateTime"));


        List<SystemConfig> systemConfigList = systemConfigRepositpory.findAll(new Specification<SystemConfig>() {
            @Override
            public Predicate toPredicate(Root<SystemConfig> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicate = new ArrayList<>();
                if (!StringUtils.isEmpty(languageType)) {
                    predicate.add(criteriaBuilder.equal(root.get("languageType"), languageType));
                }
                predicate.add(criteriaBuilder.equal(root.get("type"), type));
                predicate.add(criteriaBuilder.equal(root.get("status"), 0));

                return criteriaBuilder.and(predicate.toArray(new Predicate[predicate.size()]));

            }
        }, sort);

        SystemConfig systemConfig = new SystemConfig();

        if (systemConfigList != null && systemConfigList.size() > 0){
            systemConfig = systemConfigList.get(0);
        }

        if (systemConfig == null) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.SYSTEM_CONFIG_EMPPPTY.getCode(), languageType));
        }

//        if (StringUtils.isEmpty(languageType) || languageType.equals("0")){
//            systemConfig.setContent(systemConfig.getChineseContent());
//        }else{
//            systemConfig.setContent(systemConfig.getZangContent());
//        }

        return ResultVOUtil.success(systemConfig);
    }

    /**
     * 获取通知列表
     *
     * @param languageType
     * @param page
     * @param size
     **/
    @Override
    public ResultVO getNotifkList(String languageType, Integer page, Integer size) {

        Sort sort = new Sort(Sort.Direction.DESC,"configOrder").and(new Sort(Sort.Direction.DESC, "updateTime"));

        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = 1;
        }

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<SystemConfig> notifyPage = systemConfigRepositpory.findAll(new Specification<SystemConfig>() {
            @Override
            public Predicate toPredicate(Root<SystemConfig> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicate = new ArrayList<>();
                if (!StringUtils.isEmpty(languageType)) {
                    predicate.add(criteriaBuilder.equal(root.get("languageType"), languageType));
                }
                predicate.add(criteriaBuilder.equal(root.get("type"), 3));
                predicate.add(criteriaBuilder.equal(root.get("status"), 0));


                return criteriaBuilder.and(predicate.toArray(new Predicate[predicate.size()]));

            }
        }, pageable);

        PageInfoDTO pageInfoDTO = new PageInfoDTO();
        pageInfoDTO.setTotalPages(notifyPage.getTotalPages());
        pageInfoDTO.setTotalElements((int) notifyPage.getTotalElements());
        pageInfoDTO.setPage(page);
        pageInfoDTO.setSize(size);
        pageInfoDTO.setList(notifyPage.getContent());

        return ResultVOUtil.success(pageInfoDTO);

    }

    /**
     * 获取帮助中心列表
     *
     * @param languageType
     * @param page
     * @param size
     **/
    @Override
    public ResultVO getHelpCenterList(String languageType, Integer page, Integer size) {


        Sort sort = new Sort(Sort.Direction.DESC,"configOrder").and(new Sort(Sort.Direction.DESC, "updateTime"));

        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, size, sort);


        //1关于我们 2注册协议3通知公告4帮助中心
        Page<SystemConfig> notifyPage = systemConfigRepositpory.findAll(new Specification<SystemConfig>() {
            @Override
            public Predicate toPredicate(Root<SystemConfig> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicate = new ArrayList<>();
                if (!StringUtils.isEmpty(languageType)) {
                    predicate.add(criteriaBuilder.equal(root.get("languageType"), languageType));
                }
                predicate.add(criteriaBuilder.equal(root.get("type"), 4));
                predicate.add(criteriaBuilder.equal(root.get("status"), 0));

                return criteriaBuilder.and(predicate.toArray(new Predicate[predicate.size()]));

            }
        }, pageable);

        PageInfoDTO pageInfoDTO = new PageInfoDTO();
        pageInfoDTO.setTotalPages(notifyPage.getTotalPages());
        pageInfoDTO.setTotalElements((int) notifyPage.getTotalElements());
        pageInfoDTO.setPage(page);
        pageInfoDTO.setSize(size);
        pageInfoDTO.setList(notifyPage.getContent());

        return ResultVOUtil.success(pageInfoDTO);
    }

    /**
     * 客户端轮询检测用户是其他地方登陆
     *
     * @param languageType
     * @param userId
     * @param deviceId
     **/
    @Override
    public ResultVO checkLogin(String languageType, Integer userId, String deviceId) {

        if (userId == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }

        if (StringUtils.isEmpty(deviceId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.DEVICE_ID_NOT_EMPTY.getCode(), languageType));
        }

        String nowDeviceId = stringRedisTemplate.opsForValue().get(USER_DEVICE + userId);

        if (StringUtils.isEmpty(nowDeviceId)){
            Optional<UserInfo> byId = userInfoRepository.findById(userId);

            if (!byId.isPresent()){
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
            }
            UserInfo userInfo = byId.get();
            nowDeviceId = userInfo.getDeviceId();
        }

        if (nowDeviceId == null){
            nowDeviceId = "";
        }

        if (nowDeviceId.equals(deviceId)){
            return ResultVOUtil.success(0);
        }else {
            return ResultVOUtil.success(1);
        }

    }

    private void setUserIsMember(UserInfo userInfo) {
        if (userInfo.getMemberBeginTime() != null
                && userInfo.getMemberEndTime() != null) {
            if (CommonUtlis.isEffectiveDate(new Date(), userInfo.getMemberBeginTime(), userInfo.getMemberEndTime())) {
                userInfo.setIsMember(1);
                userInfo.setVipIcon(CommonUtlis.VIPICON1);
            } else {
                userInfo.setIsMember(2);
                userInfo.setVipIcon(CommonUtlis.VIPICON2);
            }
        } else {
            userInfo.setIsMember(0);
            userInfo.setVipIcon(CommonUtlis.VIPICON0);
        }
    }

}
