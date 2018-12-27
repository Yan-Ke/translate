package com.sian.translate.management.member.service.impl;

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

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;
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

    @Autowired
    MemberConfigRepository memberConfigRepository;

    @Autowired
    MemberPayRecordRepository memberpayRecordRepository;




    @Override
    public ResultVO getMemberList(Integer isMember, Integer page, Integer size, HttpSession session) {

        String languageType  = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }

        if (page < 1){
            page = 1;
        }
        if (size < 1){
            size = 1;
        }
        List<UserInfo> content ; // 数据列表

        if (isMember == 0){
            content = userInfoRepository.findAllUnmemberUsers((page-1)*size,size);
        }else if (isMember == 1){
            content = userInfoRepository.findAllMemberUsers((page-1)*size,size);
        }else{
            content = userInfoRepository.findAll((page-1)*size,size);
        }


        int totalElements = (int) userInfoRepository.count(); //总条数
        int totalPages =  totalElements % size == 0 ? totalElements / size : totalElements / size +1 ;// 总页数
        content.stream().forEach(user -> setUserIsMember(user));

        PageInfoDTO pageInfoDto =  new PageInfoDTO();
        pageInfoDto.setPage(page);
        pageInfoDto.setSize(size);
        pageInfoDto.setTotalElements(totalElements);
        pageInfoDto.setTotalPages(totalPages);
        pageInfoDto.setList(content);

        return ResultVOUtil.success(pageInfoDto);
    }

    @Override
    public ResultVO addMemberConfig(String explainChinese, String explainZang, String amount, Integer month, HttpSession session) {
        String languageType  = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }
        if (StringUtils.isEmpty(explainChinese)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.EXPLAIN_CHINESE_NOT_EMPTY.getCode(), languageType));
        }
        if (StringUtils.isEmpty(explainZang)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.EXPLAIN_ZANG_NOT_EMPTY.getCode(), languageType));
        }


        if (StringUtils.isEmpty(amount)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MEMBER_AMOUNT_NOT_EMPTY.getCode(), languageType));
        }
        BigDecimal amountBigDecimal = null;
        try {
            amountBigDecimal = new BigDecimal(amount);
            amountBigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP);
        }catch (Exception e){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MEMBER_AMOUNT_NOT_EMPTY.getCode(), languageType));
        }


        if (StringUtils.isEmpty(month)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MEBER_MONTH_NOT_EMPTY.getCode(), languageType));
        }

        MemberConfig memberConfig = new MemberConfig();
        memberConfig.setExplainChinese(explainChinese);
        memberConfig.setExplainZang(explainZang);
        memberConfig.setAmount(amountBigDecimal);
        memberConfig.setMonth(month);
        memberConfig.setCreateUser(userId);
        memberConfig.setUpdateUser(userId);
        memberConfig.setCreateTime(new Date());
        memberConfig.setUpdateTime(new Date());

        memberConfigRepository.save(memberConfig);

        return ResultVOUtil.success(memberConfig);
    }

    @Override
    public ResultVO editMemberConfig(Integer id,String explainChinese, String explainZang, String amount, Integer month, HttpSession session) {
        String languageType  = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }
        if (StringUtils.isEmpty(id)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MEBER_CONIFG_ID_NOT_EMPTY.getCode(), languageType));
        }
        Optional<MemberConfig> memberConfigOptional = memberConfigRepository.findById(id);

        if (!memberConfigOptional.isPresent()){{
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MEBER_CONIFG_IS_NULL.getCode(), languageType));
        }}

        MemberConfig memberConfig = memberConfigOptional.get();

        if (!StringUtils.isEmpty(explainChinese)){
            memberConfig.setExplainChinese(explainChinese);
        }
        if (!StringUtils.isEmpty(explainZang)){
            memberConfig.setExplainZang(explainZang);
        }

        if (!StringUtils.isEmpty(amount)){

            BigDecimal amountBigDecimal = null;
            try {
                amountBigDecimal = new BigDecimal(amount);
                amountBigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP);
            }catch (Exception e){
                e.printStackTrace();
            }
            if (amountBigDecimal != null ){
                memberConfig.setAmount(amountBigDecimal);
            }

        }

        if (!StringUtils.isEmpty(month)){
            memberConfig.setMonth(month);
        }

        memberConfig.setUpdateUser(userId);
        memberConfig.setUpdateTime(new Date());

        memberConfigRepository.save(memberConfig);

        return ResultVOUtil.success(memberConfig);
    }

    @Override
    public ResultVO getAllMemberPayRecordList(Integer page, Integer size, HttpSession session) {
        String languageType  = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        if (page < 1){
            page = 1;
        }
        if (size < 1){
            size = 1;
        }


        List<Object[]> result  = memberpayRecordRepository.findAllMemberRecords((page - 1) *size,size);

        List<UserRecordDTO> content = CommonUtlis.castEntity(result, UserRecordDTO.class, new UserRecordDTO());

        int totalElements = (int) memberpayRecordRepository.count(); //总条数
        int totalPages =  totalElements % size == 0 ? totalElements / size : totalElements / size +1 ;// 总页数

        PageInfoDTO pageInfoDto =  new PageInfoDTO();
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
