package com.sian.translate.user.service.impl;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.user.entity.SystemConfig;
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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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

    @Override
    public ResultVO editUserInfo(MultipartFile file, UserInfo userInfo, String type) {


        if (StringUtils.isEmpty(userInfo.getId())) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), type));
        }

        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(userInfo.getId());
        if (!userInfoOptional.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), type));
        }
        UserInfo resultUserInfo = userInfoOptional.get();

        if (!StringUtils.isEmpty(userInfo.getNickName())) {
            resultUserInfo.setNickName(userInfo.getNickName());
        }

//        if (!StringUtils.isEmpty(userInfo.getPhone())) {
//            resultUserInfo.setNickName(userInfo.getPhone());
//        }

        if (userInfo.getSex() != null) {
            resultUserInfo.setSex(userInfo.getSex());
        }
        if (userInfo.getAge() != null) {
            resultUserInfo.setAge(userInfo.getAge());
        }

        if (!StringUtils.isEmpty(userInfo.getEducation())) {
            resultUserInfo.setEducation(userInfo.getEducation());
        }

        if (file != null && !file.isEmpty()) {
            try {
                HashMap<String, String> map = ImageUtlis.loadImageAndcompressImg(file);
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
    public ResultVO login(String phone, String code, String type) {

        if (StringUtils.isEmpty(phone))
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PHONE_NOT_EMPTY.getCode(), type));
        if (StringUtils.isEmpty(code))
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.CODE_NOT_EMPTY.getCode(), type));

        UserInfo userInfo = userInfoRepository.findByPhone(phone);
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setNickName(phone);
            userInfo.setSex(0);
            userInfo.setAge(0);
            userInfo.setBalance(new BigDecimal(0.00));
            userInfo.setRegistrationTime(new Date());
            userInfo.setUpdateTime(new Date());
            userInfo.setUserStatus(0);
        }else{
            if (userInfo.getUserStatus() == 1){
                ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_PROHIBIT.getCode(), type));
            }
        }

        setUserIsMember(userInfo);
        userInfo.setLoginTime(new Date());
        userInfoRepository.save(userInfo);

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

        List<String> educationNameList = new ArrayList<>();

        if (!StringUtils.isEmpty(languageType) && languageType.equals("1")) {
            if (educationRepository.getAllZangEducationName() != null) {
                educationNameList.addAll(educationRepository.getAllZangEducationName());
            }
        } else {
            if (educationRepository.getAllChineseEducationName() != null) {
                educationNameList.addAll(educationRepository.getAllChineseEducationName());
            }
        }
        return ResultVOUtil.success(educationNameList);

    }

    /**
     * 用户帮助反馈
     *
     * @param languageType
     * @param userId
     * @param content
     **/
    @Override
    public ResultVO feedback(String languageType, Integer userId, String content,MultipartFile[] files) {

        if (userId == null) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }
        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(userId);

        if (!userInfoOptional.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }
        if (StringUtils.isEmpty(content) || StringUtils.isEmpty(content.trim())) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.FEED_BACK_NOT_EMPTY.getCode(), languageType));
        }
        if (content.length() > 200) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.FEED_BACK_TO_LONG.getCode(), languageType));
        }


        String images = "";

        if (files != null && files.length > 0 ) {
            try {

                if (files != null && files.length > 0) {
                    for (int i = 0; i < files.length; i++) {
                        MultipartFile file = files[i];
                        // 保存文件
                        images += ImageUtlis.loadImage(file) +",";

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

        if (!StringUtils.isEmpty(images)){
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

        if (count > 0) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PHONE_USEED.getCode(), languageType));
        }

        if (StringUtils.isEmpty(userInfo.getNickName())) {
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
        userInfoRepository.save(userInfo);
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
    public ResultVO thridLogin(String weixinOpenid, String qqOpenid, String languageType) {


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
                if (userInfo.getUserStatus() == 1){
                    ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_PROHIBIT.getCode(), languageType));
                }
                setUserIsMember(userInfo);
                userInfo.setLoginTime(new Date());
                userInfoRepository.save(userInfo);
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
        if (id == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }
        if (StringUtils.isEmpty(nowPhone)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOW_PHONE_IS_NOT_EMPTY.getCode(), languageType));
        }
        if (StringUtils.isEmpty(newPhone)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NEW_PHONE_IS_NOT_EMPTY.getCode(), languageType));
        }
        if (StringUtils.isEmpty(code)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.CODE_NOT_EMPTY.getCode(), languageType));
        }

        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(id);
        if (!userInfoOptional.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }
        int count = userInfoRepository.countByPhone(newPhone);

        if (count > 0){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PHONE_USEED.getCode(), languageType));
        }

        UserInfo userInfo = userInfoOptional.get();

        if (!userInfo.getPhone().trim().equals(nowPhone.trim())){
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

        if (StringUtils.isEmpty(type)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.SYSTEM_CONFIG_TYPE_NOT_EMPPPTY.getCode(), languageType));
        }
        SystemConfig systemConfig = systemConfigRepositpory.findByType(type);

        if (systemConfig == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.SYSTEM_CONFIG_EMPPPTY.getCode(), languageType));
        }

        if (StringUtils.isEmpty(languageType) || languageType.equals("0")){
            systemConfig.setContent(systemConfig.getChineseContent());
        }else{
            systemConfig.setContent(systemConfig.getZangContent());
        }

        return ResultVOUtil.success(systemConfig);
    }

    private void setUserIsMember(UserInfo userInfo) {
        if (userInfo.getMemberBeginTime() != null
                && userInfo.getMemberEndTime() != null) {
            if (CommonUtlis.isEffectiveDate(new Date(), userInfo.getMemberBeginTime(), userInfo.getMemberEndTime())){
                userInfo.setIsMember(1);
            }else{
                userInfo.setIsMember(2);
            }
        }else{
            userInfo.setIsMember(0);
        }
    }

}
