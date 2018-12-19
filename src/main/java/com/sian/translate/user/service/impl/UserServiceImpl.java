package com.sian.translate.user.service.impl;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.user.entity.UserFeedback;
import com.sian.translate.user.entity.UserInfo;
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
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Override
    public ResultVO registe(MultipartFile file, UserInfo userInfo, String type) {

        //返回上传的文件是否为空，即没有选择任何文件，或者所选文件没有内容。
        //防止上传空文件导致奔溃
        if (file.isEmpty()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.IMG_NOT_EMPTY.getCode(), type));
        }
        if (StringUtils.isEmpty(userInfo.getNickName())){

        }

        if (StringUtils.isEmpty(userInfo.getPhone())){

        }

        if (StringUtils.isEmpty(userInfo.getPassword())){

        }
        if (userInfo.getSex() == null){

        }
        if (userInfo.getAge() == null){

        }
        if (StringUtils.isEmpty(userInfo.getEducation())){

        }

        try {
            HashMap<String, String> map = ImageUtlis.loadImage(file);
            if (map.isEmpty()){
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.IMG_FORMAT_ERROR.getCode(), type));
            }
            userInfo.setHeadSmallImage(map.get("smallFile"));
            userInfo.setHeadBigImage(map.get("bigFile"));
            userInfo.setOrignalImage(map.get("orignalFile"));

        } catch (IOException e) {
            e.printStackTrace();
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.IMG_FORMAT_ERROR.getCode(), type));
        }

        String password = userInfo.getPassword();
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        userInfo.setPassword(md5Password);
        userInfo.setRegistrationTime(new Date());
        userInfo.setUpdateTime(new Date());
        userInfo.setIsMember(0);
        userInfoRepository.save(userInfo);
        //返回客户端不加密密码方便登陆
//        userInfo.setPassword(password);

        return ResultVOUtil.success(userInfo);
    }


    /*******
     *
     * @param phone 手机号码
     * @param password 密码
     * @param type 0中文 1藏文
     * @return
     */
    @Override
    public ResultVO login(String phone, String password, String type) {

        if (StringUtils.isEmpty(phone))
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PHONE_NOT_EMPTY.getCode(), type));
        if (StringUtils.isEmpty(password))
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PASSWORD_NOT_EMPTY.getCode(), type));

        UserInfo userInfo = userInfoRepository.findByPhone(phone);
        if (userInfo == null)
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), type));

        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Password.equals(userInfo.getPassword())) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PASSWORD_ERROR.getCode(), type));
        }
        userInfo.setIsMember(0);
        if (userInfo.getMemberBeginTime() != null
                && userInfo.getMemberEndTime() != null
                && CommonUtlis.isEffectiveDate(new Date(), userInfo.getMemberBeginTime(), userInfo.getMemberEndTime())) {
            userInfo.setIsMember(1);
        }
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
        userInfo.setIsMember(0);
        if (userInfo.getMemberBeginTime() != null
                && userInfo.getMemberEndTime() != null
                && CommonUtlis.isEffectiveDate(new Date(), userInfo.getMemberBeginTime(), userInfo.getMemberEndTime())) {
            userInfo.setIsMember(1);
        }

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

        if (!StringUtils.isEmpty(languageType) && languageType.equals("1")){
            if (educationRepository.getAllZangEducationName() != null){
                educationNameList.addAll(educationRepository.getAllZangEducationName());
            }
        }else{
            if (educationRepository.getAllChineseEducationName() != null){
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
    public ResultVO feedback(String languageType, Integer userId, String content) {

        if (userId == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }
        if (StringUtils.isEmpty(content) || StringUtils.isEmpty(content.trim())){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.FEED_BACK_NOT_EMPTY.getCode(), languageType));
        }
        if (content.length() > 300){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.FEED_BACK_TO_LONG.getCode(), languageType));
        }

        UserFeedback userFeedback = new UserFeedback();
        userFeedback.setUserId(userId);
        userFeedback.setContent(content);
        userFeedback.setCreateTime(new Date());
        userFeedbackRepositpory.save(userFeedback);

        return null;
    }


}
