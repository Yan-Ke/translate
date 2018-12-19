package com.sian.translate.management.member.service.impl;

import com.sian.translate.DTO.PageInfoDto;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.management.member.service.ManageMemberService;
import com.sian.translate.management.user.entity.ManageUserInfo;
import com.sian.translate.management.user.repository.ManageUserInfoRepository;
import com.sian.translate.management.user.service.ManageUserService;
import com.sian.translate.member.enity.MemberPayRecord;
import com.sian.translate.user.entity.UserInfo;
import com.sian.translate.user.repository.UserInfoRepository;
import com.sian.translate.utlis.CommonUtlis;
import com.sian.translate.utlis.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ManageMemberServiceImpl implements ManageMemberService {


    @Autowired
    HintMessageService hintMessageService;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    ManageUserInfoRepository manageUserInfoRepository;


    @Override
    public ResultVO getMemberList(Integer isMember, Integer page, Integer size, HttpSession session) {

        String languageType  = "0";

        if (StringUtils.isEmpty(session.getAttribute(ManageUserService.SESSION_KEY))){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }


        Sort sort = new Sort(Sort.Direction.DESC, "registration_time");
        if (page < 1){
            page = 1;
        }
        if (size < 1){
            size = 1;
        }
        Pageable pageable = PageRequest.of(page-1,size,sort);

        Page<UserInfo> datas;
        if (isMember == 0){
            datas = userInfoRepository.findAllUnmemberUsers(pageable);
        }else if (isMember == 1){
            datas = userInfoRepository.findAllMemberUsers(pageable);
        }else{
            pageable = PageRequest.of(page-1,size,new Sort(Sort.Direction.DESC, "registrationTime"));
            datas = userInfoRepository.findAll(pageable);
        }


        int totalElements = (int) datas.getTotalElements(); //总条数
        int totalPages =  datas.getTotalPages(); // 总页数
        List<UserInfo> content = datas.getContent(); // 数据列表
        content.stream().forEach(user -> setUserIsMember(user));

        PageInfoDto pageInfoDto =  new PageInfoDto();
        pageInfoDto.setPage(page);
        pageInfoDto.setSize(size);
        pageInfoDto.setTotalElements(totalElements);
        pageInfoDto.setTotalPages(totalPages);
        pageInfoDto.setList(content);

        return ResultVOUtil.success(pageInfoDto);
    }

    private void setUserIsMember(UserInfo user) {
        user.setIsMember(1);
        if (user.getMemberBeginTime() == null || CommonUtlis.compareNowDate(user.getMemberBeginTime()) == -1)
             user.setIsMember(0);
    }
}
