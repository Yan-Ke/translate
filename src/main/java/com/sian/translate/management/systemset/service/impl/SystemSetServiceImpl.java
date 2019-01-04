package com.sian.translate.management.systemset.service.impl;

import com.sian.translate.DTO.PageInfoDTO;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.management.log.enity.ManageLog;
import com.sian.translate.management.log.repositiory.ManageLogRepositiory;
import com.sian.translate.management.systemset.enity.HelpCenter;
import com.sian.translate.management.systemset.enity.Notify;
import com.sian.translate.management.systemset.repository.HelpCenterRepository;
import com.sian.translate.management.systemset.repository.NotifyRepository;
import com.sian.translate.management.systemset.service.SystemSetService;
import com.sian.translate.management.user.entity.ManageUserInfo;
import com.sian.translate.management.user.repository.ManageUserInfoRepository;
import com.sian.translate.management.user.service.ManageUserService;
import com.sian.translate.user.entity.SystemConfig;
import com.sian.translate.user.entity.UserFeedback;
import com.sian.translate.user.repository.SystemConfigRepositpory;
import com.sian.translate.user.repository.UserFeedbackRepositpory;
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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
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
    NotifyRepository notifyRepository;

    @Autowired
    HelpCenterRepository helpCenterRepository;

    @Autowired
    SystemConfigRepositpory systemConfigRepositpory;

    @Autowired
    ManageLogRepositiory manageLogRepositiory;

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
    public ResultVO addNotify(Integer languageType, String title, String content, HttpSession session) {


        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType+""));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType+""));
        }

        if (StringUtils.isEmpty(title)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOTIFY_TITLE_NOT_EMPTY.getCode(), languageType+""));
        }
        if (StringUtils.isEmpty(content)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOTIFY_CONTENT_NOT_EMPTY.getCode(), languageType+""));
        }

        Notify notify = new Notify();
        notify.setTitle(title);
        notify.setContent(content);
        notify.setLanguageType(languageType);
        notify.setCreateUser(userId);
        notify.setUpdateUser(userId);
        notify.setCreateTime(new Date());
        notify.setUpdateTime(new Date());
        notifyRepository.save(notify);

        String losmsg = "新增通知公告"+notify.getTitle();


        return ResultVOUtil.success(notify,losmsg);
    }

    @Override
    public ResultVO deleteNotify(Integer id, HttpSession session) {

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
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOTIFY_ID_NOT_EMPTY.getCode(), languageType));
        }

        Optional<Notify> byId = notifyRepository.findById(id);

        if (!byId.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOTIFY_IS_NOT_EXIST.getCode(), languageType));
        }
        Notify notify = byId.get();

        notifyRepository.delete(notify);

        String losmsg = "删除通知公告"+notify.getTitle();


        return ResultVOUtil.success(losmsg);
    }

    @Override
    public ResultVO getNotifykList(String title, Integer page, Integer size, HttpSession session) {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }


        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");

        if (page < 1){
            page = 1;
        }
        if (size < 1){
            size = 1;
        }
        Pageable pageable = PageRequest.of(page - 1 , size, sort);

        Page<Notify> notifyPage = notifyRepository.findAll(new Specification<Notify>() {
            @Override
            public Predicate toPredicate(Root<Notify> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicate = new ArrayList<>();
                if (!StringUtils.isEmpty(title)) {
                    predicate.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
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
    public ResultVO getHelpCenterList(String title, Integer page, Integer size, HttpSession session) {
        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }


        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");

        if (page < 1){
            page = 1;
        }
        if (size < 1){
            size = 1;
        }
        Pageable pageable = PageRequest.of(page - 1 , size, sort);

        Page<HelpCenter> notifyPage = helpCenterRepository.findAll(new Specification<HelpCenter>() {
            @Override
            public Predicate toPredicate(Root<HelpCenter> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicate = new ArrayList<>();
                if (!StringUtils.isEmpty(title)) {
                    predicate.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
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
    public ResultVO addHelpCenter(String title, String content, Integer status,HttpSession session) {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }

        if (StringUtils.isEmpty(title)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.HELP_TITLE_NOT_EMPTY.getCode(), languageType));
        }
        if (StringUtils.isEmpty(content)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.HELP_CONTENT_NOT_EMPTY.getCode(), languageType));
        }
        if (status == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.HELP_STATUS_NOT_EMPTY.getCode(), languageType));
        }

        HelpCenter helpCenter = new HelpCenter();
        helpCenter.setTitle(title);
        helpCenter.setContent(content);
        helpCenter.setStatus(status);
        helpCenter.setCreateUser(userId);
        helpCenter.setUpdateUser(userId);
        helpCenter.setCreateTime(new Date());
        helpCenter.setUpdateTime(new Date());

        helpCenterRepository.save(helpCenter);

        String losmsg = "新增帮助"+helpCenter.getTitle();


        return ResultVOUtil.success(helpCenter,losmsg);
    }

    @Override
    public ResultVO deleteHelpCenter(Integer id, HttpSession session) {
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

        Optional<HelpCenter> byId = helpCenterRepository.findById(id);

        if (!byId.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.HELPIS_NOT_EXIST.getCode(), languageType));
        }
        HelpCenter helpCenter = byId.get();

        helpCenterRepository.delete(helpCenter);

        String losmsg = "删除帮助"+helpCenter.getTitle();

        return ResultVOUtil.success(losmsg);
    }

    @Transactional
    @Override
    public ResultVO addRegstieProtocol(String content, HttpSession session) {
        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }

        if (StringUtils.isEmpty(content)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.REGISTE_PROTOCOL_CONTENT_NOT_EMPTY.getCode(), languageType));
        }

        systemConfigRepositpory.deleteByType(2);

        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setContent(content);
        systemConfig.setType(2);
        systemConfig.setCreateUser(userId);
        systemConfig.setUpdateUser(userId);
        systemConfig.setCreateTime(new Date());
        systemConfig.setUpdateTime(new Date());

        systemConfigRepositpory.save(systemConfig);

        String losmsg = "添加注册协议";


        return ResultVOUtil.success(losmsg);
    }

    @Override
    public ResultVO getRegstieProtocol(HttpSession session) {
        return getResultVO(session, 2);
    }


    @Transactional
    @Override
    public ResultVO addAboutMe(String phone, String content, HttpSession session) {
        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }

        if (StringUtils.isEmpty(content)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ABOUT_ME_PHONE_NOT_EMPTY.getCode(), languageType));
        }
        if (StringUtils.isEmpty(content)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ABOUT_ME_CONTENT_NOT_EMPTY.getCode(), languageType));
        }

        systemConfigRepositpory.deleteByType(1);

        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setPhone(phone);
        systemConfig.setContent(content);
        systemConfig.setType(1);
        systemConfig.setCreateUser(userId);
        systemConfig.setUpdateUser(userId);
        systemConfig.setCreateTime(new Date());
        systemConfig.setUpdateTime(new Date());

        systemConfigRepositpory.save(systemConfig);

        String losmsg = "添加关于我们";


        return ResultVOUtil.success(losmsg);
    }

    @Override
    public ResultVO getAboutMe(HttpSession session) {
        return getResultVO(session, 1);
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

    private ResultVO getResultVO(HttpSession session, int i) {
        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }

        SystemConfig systemConfig = systemConfigRepositpory.findByType(i);

        return ResultVOUtil.success(systemConfig);
    }
}
