package com.sian.translate.management.systemset.service.impl;

import com.sian.translate.DTO.PageInfoDTO;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.dictionary.enity.UserTranslateRecord;
import com.sian.translate.dictionary.repository.UserTranslateRecordRepository;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.management.log.enity.ManageLog;
import com.sian.translate.management.log.repositiory.ManageLogRepositiory;
import com.sian.translate.management.systemset.service.SystemSetService;
import com.sian.translate.management.user.entity.ManageUserInfo;
import com.sian.translate.management.user.repository.ManageUserInfoRepository;
import com.sian.translate.management.user.service.ManageUserService;
import com.sian.translate.user.entity.SystemConfig;
import com.sian.translate.user.entity.UserFeedback;
import com.sian.translate.user.repository.SystemConfigRepositpory;
import com.sian.translate.user.repository.UserFeedbackRepositpory;
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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SystemSetServiceImpl implements SystemSetService {


    @Autowired
    HintMessageService hintMessageService;

    @Autowired
    ManageUserInfoRepository manageUserInfoRepository;

    @Autowired
    UserFeedbackRepositpory userFeedbackRepositpory;


    @Autowired
    SystemConfigRepositpory systemConfigRepositpory;

    @Autowired
    ManageLogRepositiory manageLogRepositiory;

    @Autowired
    UserTranslateRecordRepository userTranslateRecordRepository;

    @Override
    public ResultVO getFeedbackList(Integer status, String nickName, String content, Integer page, Integer size, HttpSession session) {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }

        Sort sort = new Sort(Sort.Direction.DESC,"createTime");

        if (page < 1){
            page = 1;
        }
        if (size < 1){
            size = 1;
        }
        Pageable pageable = PageRequest.of(page-1, size, sort);

        Specification<UserFeedback> specification = (Specification<UserFeedback>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicate = new ArrayList<>();
            if(!StringUtils.isEmpty(nickName)) {
                predicate.add(criteriaBuilder.like(root.get("userNickName"),"%"+nickName+"%"));
            }
            if(status!=null) {
                predicate.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if(!StringUtils.isEmpty(content)) {
                predicate.add(criteriaBuilder.like(root.get("content"),"%"+content+"%"));
            }

            return criteriaBuilder.and(predicate.toArray(new Predicate[predicate.size()]));
        };

        Page<UserFeedback> all = userFeedbackRepositpory.findAll(specification, pageable);

       List<UserFeedback> userFeedbackList =  all.getContent();

        for (UserFeedback userFeedback : userFeedbackList) {
            String iamges = userFeedback.getImage();

            List<String> imageList = new ArrayList<>();

            if (!StringUtils.isEmpty(iamges)){

                String[] split = iamges.split(",");

                if (split != null && split.length > 0 ){
                    for (String s : split) {
                        imageList.add(s);
                    }
                }
            }
            userFeedback.setImages(imageList);

        }

        PageInfoDTO pageInfoDTO = new PageInfoDTO();
        pageInfoDTO.setTotalElements((int) all.getTotalElements());
        pageInfoDTO.setTotalPages(all.getTotalPages());
        pageInfoDTO.setPage(page);
        pageInfoDTO.setSize(size);
        pageInfoDTO.setList(userFeedbackList);

        return ResultVOUtil.success(pageInfoDTO);
    }

    @Override
    public ResultVO handleFeedback(Integer id, HttpSession session) {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }

        if (id == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.FEEDBACK_ID_IS_NOT_NULL.getCode(), languageType));
        }

        Optional<UserFeedback> byId = userFeedbackRepositpory.findById(id);

        if (!byId.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.FEEDBACK_IS_NOT_EXIST.getCode(), languageType));
        }

        UserFeedback userFeedback = byId.get();

        if (userFeedback.getStatus() == 1){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.FEEDBACK_IS_HANDER1072.getCode(), languageType));
        }

        userFeedback.setStatus(1);
        userFeedbackRepositpory.save(userFeedback);


        return ResultVOUtil.success();
    }







    @Override
    public ResultVO getManageLogList(Integer page, Integer size, HttpSession session) {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }


        Sort sort = new Sort(Sort.Direction.DESC,"createTime");

        if (page < 1){
            page = 1;
        }
        if (size < 1){
            size = 1;
        }
        Pageable pageable = PageRequest.of(page - 1 , size, sort);

        Page<ManageLog> manageLogs = manageLogRepositiory.findAllByTypeIsNot(4,pageable);

        PageInfoDTO pageInfoDTO = new PageInfoDTO();
        pageInfoDTO.setTotalPages(manageLogs.getTotalPages());
        pageInfoDTO.setTotalElements((int) manageLogs.getTotalElements());
        pageInfoDTO.setPage(page);
        pageInfoDTO.setSize(size);
        pageInfoDTO.setList(manageLogs.getContent());

        return ResultVOUtil.success(pageInfoDTO);
    }

    @Override
    public ResultVO uploadImage(MultipartFile file, HttpServletRequest request) {
        String imagePath = "";

        if (file != null && !file.isEmpty()){
            try {

                imagePath = ImageUtlis.loadImage(file,request);
            } catch (IOException e) {
                e.printStackTrace();
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.IMG_FORMAT_ERROR.getCode(), "0"));
            }
        }

        return ResultVOUtil.uploadSuccess(imagePath);
    }


    @Override
    public ResultVO addFile(Integer type, String languageType, String field, String content, Integer status, Integer order, HttpSession session) {


        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }


        if (type == null || type < 1){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.FILE_TYPE_NOT_EMPTY.getCode(), languageType));
        }

        if (StringUtils.isEmpty(field)){
            if (type == 1){
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.FILE_FILED_NOT_EMPTY.getCode(), languageType));
            }else {
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ABOUT_ME_PHONE_NOT_EMPTY.getCode(), languageType));
            }
        }
        if (content == null || content.trim().equals("")){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.FILE_CONTENT_NOT_EMPTY.getCode(), languageType));
        }

        if (type == 1){
            systemConfigRepositpory.deleteAllByType(1);
        }

        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setField(field);
        systemConfig.setContent(content);
        systemConfig.setType(type);
        systemConfig.setStatus(status);
        systemConfig.setConfigOrder(order);
        systemConfig.setLanguageType(Integer.valueOf(languageType));
        systemConfig.setCreateUser(userId);
        systemConfig.setUpdateUser(userId);
        systemConfig.setCreateTime(new Date());
        systemConfig.setUpdateTime(new Date());

        systemConfigRepositpory.save(systemConfig);


        //1关于我们 2注册协议3通知公告4帮助中心

        String losmsg = "";
        switch (type){
            case 1:
                losmsg = "添加关于我们";
                break;
            case 2:
                losmsg = "添加注册协议"+"文档名称:("+field+")";
                break;
            case 3:
                losmsg = "添加通知公告"+"文档名称:("+field+")";
                break;
            case 4:
                losmsg = "添加帮助中心"+"文档名称:("+field+")";
                break;
        }


        return ResultVOUtil.success(losmsg);
    }

    @Override
    public ResultVO deleteFile(Integer id, HttpSession session) {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }
        if (StringUtils.isEmpty(id)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.HELP_ID_NOT_EMPTY.getCode(), languageType));
        }

        Optional<SystemConfig> byId = systemConfigRepositpory.findById(id);

        if (!byId.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.HELPIS_NOT_EXIST.getCode(), languageType));
        }
        SystemConfig systemConfig = byId.get();


        systemConfigRepositpory.delete(systemConfig);

        String losmsg = "";
        String field = systemConfig.getField();
        switch (systemConfig.getType()){
            case 1:
                losmsg = "删除关于我们";
                break;
            case 2:
                losmsg = "删除注册协议"+"文档名称:("+field+")";
                break;
            case 3:
                losmsg = "删除通知公告"+"文档名称:("+field+")";
                break;
            case 4:
                losmsg = "删除帮助中心"+"文档名称:("+field+")";
                break;
        }

        return ResultVOUtil.success(losmsg);
    }

    @Transactional
    @Override
    public ResultVO editFile(Integer id, Integer type, String languageType, String field, String content, Integer status, Integer order, String email, String qq, String weixin, HttpSession session) {
        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }


        if (id == null && type != 1){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.FILE_IS_NOT_EXIST.getCode(), languageType));
        }
        if (id == null){
            id = 0;
        }
        Optional<SystemConfig> byId = systemConfigRepositpory.findById(id);

        if (!byId.isPresent() && type != 1){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.FILE_IS_NOT_EXIST.getCode(), languageType));
        }

        if (type == null || type < 1){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.FILE_TYPE_NOT_EMPTY.getCode(), languageType));
        }

        if (StringUtils.isEmpty(field)){
            if (type != 1){
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.FILE_FILED_NOT_EMPTY.getCode(), languageType));
            }else {
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ABOUT_ME_PHONE_NOT_EMPTY.getCode(), languageType));
            }
        }
        if (type == 1){
            if (StringUtils.isEmpty(email)) {
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.EMAIL_NOT_EMPTY.getCode(), languageType));
            }
            if (StringUtils.isEmpty(qq)) {
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.QQ_NOT_EMPTY.getCode(), languageType));
            }
            if (StringUtils.isEmpty(weixin)) {
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.WEIXIN_NOT_EMPTY.getCode(), languageType));
            }
        }

        if (content == null || content.trim().equals("")){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.FILE_CONTENT_NOT_EMPTY.getCode(), languageType));
        }
        SystemConfig systemConfig;

        if (type == 1){
            systemConfig = new SystemConfig();
            systemConfig.setCreateUser(userId);
            systemConfig.setEmail(email);
            systemConfig.setQq(qq);
            systemConfig.setWeixin(weixin);
            systemConfig.setCreateTime(new Date());
            systemConfigRepositpory.deleteAllByType(1);
        }else{
            systemConfig = byId.get();
        }

        systemConfig.setField(field);
        systemConfig.setContent(content);
        systemConfig.setType(type);
        systemConfig.setStatus(status);
        systemConfig.setLanguageType(Integer.valueOf(languageType));
        systemConfig.setConfigOrder(order);
        systemConfig.setUpdateUser(userId);
        systemConfig.setUpdateTime(new Date());

//        if (type == 1){
//            systemConfigRepositpory.deleteAllByType(1);
//        }
        systemConfigRepositpory.save(systemConfig);


        //1关于我们 2注册协议3通知公告4帮助中心

        String losmsg = "";
        switch (type){
            case 1:
                losmsg = "修改关于我们";
                break;
            case 2:
                losmsg = "修改注册协议"+"文档名称:("+field+")";
                break;
            case 3:
                losmsg = "修改通知公告"+"文档名称:("+field+")";
                break;
            case 4:
                losmsg = "修改帮助中心"+"文档名称:("+field+")";
                break;
        }


        return ResultVOUtil.success(losmsg);
    }

    @Override
    public ResultVO getTranslateList(Integer type, String field, Integer page, Integer size, HttpSession session) {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }


        Sort sort = new Sort(Sort.Direction.DESC,"createTime");

        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = 1;
        }

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<UserTranslateRecord> notifyPage = userTranslateRecordRepository.findAll(new Specification<UserTranslateRecord>() {
            @Override
            public Predicate toPredicate(Root<UserTranslateRecord> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicate = new ArrayList<>();

                switch (type){
                    case 1:
                        predicate.add(criteriaBuilder.like(root.get("content"), "%"+field+"%"));
                        break;
                    case 2:
                        predicate.add(criteriaBuilder.like(root.get("nickName"), "%"+field+"%"));
                        break;
                    case 3:
                        predicate.add(criteriaBuilder.isNull(root.get("translateContent")));
                        break;
                }

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

    @Override
    public ResultVO getFileList(Integer type, String filed, Integer page, Integer size, HttpSession session) {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }


        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }


        Sort sort = new Sort(Sort.Direction.DESC,"configOrder").and(new Sort(Sort.Direction.DESC, "createTime"));

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

                if (type != null && type != 0){
                    predicate.add(criteriaBuilder.equal(root.get("type"), type));
                }else {
                    predicate.add(criteriaBuilder.notEqual(root.get("type"), 1));

                }


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


}
