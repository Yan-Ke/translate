package com.sian.translate.management.user.service.impl;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.management.user.entity.ManageUserInfo;
import com.sian.translate.management.user.repository.ManageUserInfoRepository;
import com.sian.translate.management.user.service.ManageUserService;
import com.sian.translate.utlis.CommonUtlis;
import com.sian.translate.utlis.ImageUtlis;
import com.sian.translate.utlis.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class ManageUserServiceImpl implements ManageUserService {


    @Autowired
    ManageUserInfoRepository manageUserInfoRepository;

    @Autowired
    HintMessageService hintMessageService;



    /*******
     *
     * @param account 登录名
     * @param password 密码
     * @return
     */
    @Override
    public ResultVO login(String account, String password, HttpSession session){


        String type = "0";

        if (StringUtils.isEmpty(account))
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ACCOUNT_NOT_EMPTY.getCode(), type));
        if (StringUtils.isEmpty(password))
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PASSWORD_NOT_EMPTY.getCode(), type));



        ManageUserInfo userInfo = manageUserInfoRepository.findByAccount(account);
        if (userInfo == null)
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), type));

        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Password.equals(userInfo.getPassword())) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PASSWORD_ERROR.getCode(), type));
        }

        Integer userID = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);
        log.info("seesion={}",userID);
        session.setAttribute(ManageUserService.SESSION_KEY, userInfo.getId());


        return ResultVOUtil.success(userInfo);
    }


}
