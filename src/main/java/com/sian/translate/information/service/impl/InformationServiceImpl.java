package com.sian.translate.information.service.impl;

import com.sian.translate.DTO.PageInfoDTO;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.information.enity.Information;
import com.sian.translate.information.enity.UserMidInformation;
import com.sian.translate.information.repository.InformationRepository;
import com.sian.translate.information.repository.UserMidInformationRepoitory;
import com.sian.translate.information.service.InformationService;
import com.sian.translate.utlis.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class InformationServiceImpl implements InformationService {

    @Autowired
    InformationRepository informationRepository;

    @Autowired
    UserMidInformationRepoitory userMidInformationRepoitory;

    @Autowired
    HintMessageService hintMessageService;

    @Override
    public ResultVO getInformationList(Integer userId,String languageType,Integer informationType,Integer advLocation,Integer page ,Integer size) {

        int type = 0;
        if (!StringUtils.isEmpty(languageType) && languageType.equals("1")){
            type = 1;
        }
        if (userId == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }

        Sort sort = new Sort(Sort.Direction.DESC,"infomationOrder").and(new Sort(Sort.Direction.DESC, "createTime"));


        if (page < 1){
            page = 1;
        }
        if (size < 1){
            size = 1;
        }

        if (informationType == 1){
            page = 1;
            size = 5;
        }

        Pageable pageable = PageRequest.of(page-1,size,sort);

        Page<Information> datas;

        if (informationType == 0){
            datas = informationRepository.findAllByLanguageTypeAndIsShow(type,1,pageable);
        }else {
            datas = informationRepository.findAllByLanguageTypeAndIsShowAndTypeAndAdvLocationAndRecommend(type, 1, informationType, advLocation, 1,pageable);
        }

        int totalElements = (int) datas.getTotalElements(); //总条数
        int totalPages =  datas.getTotalPages(); // 总页数
        List<Information> content = datas.getContent(); // 数据列表

        for (Information information : content) {

            UserMidInformation userMidInformation = userMidInformationRepoitory.findByUserIdAndInfomationIdAndType(userId, information.getId(), 2);
            if (userMidInformation != null){
                information.setIsZan(1);
            }else {
                information.setIsZan(0);
            }
        }

        PageInfoDTO pageInfoDto =  new PageInfoDTO();
        pageInfoDto.setPage(page);
        pageInfoDto.setSize(size);
        pageInfoDto.setTotalElements(totalElements);
        pageInfoDto.setTotalPages(totalPages);
        pageInfoDto.setList(content);

        return ResultVOUtil.success(pageInfoDto);

    }

    @Override
    public ResultVO readInformation(String languageType, Integer informationId, Integer userId) {

        if (StringUtils.isEmpty(informationId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }
        if (StringUtils.isEmpty(informationId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.INFORMATION_ID_NOT_EMPTY.getCode(), languageType));
        }


        Optional<Information> byId = informationRepository.findById(informationId);
        if (!byId.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.INFORMATION_NOT_EXIST.getCode(), languageType));
        }
        Information information = byId.get();

        Integer lookCount = information.getLookCount();


        UserMidInformation userMidInformation = userMidInformationRepoitory.findByUserIdAndInfomationIdAndType(userId, informationId,1);

        //没有阅读过该条资讯
        if (userMidInformation == null){
            if (lookCount == null){
                lookCount = 1;
            }else {
                lookCount += 1;
            }
            information.setLookCount(lookCount);

            userMidInformation = new UserMidInformation();
            userMidInformation.setUserId(userId);
            userMidInformation.setInfomationId(informationId);
            userMidInformation.setType(1);
            userMidInformation.setCreateTime(new Date());
            userMidInformationRepoitory.save(userMidInformation);

            informationRepository.save(information);
        }



        return ResultVOUtil.success(information);
    }

    @Override
    public ResultVO zanInformation(String languageType, Integer informationId, Integer type,Integer userId) {
        if (StringUtils.isEmpty(informationId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }
        if (StringUtils.isEmpty(informationId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.INFORMATION_ID_NOT_EMPTY.getCode(), languageType));
        }


        Optional<Information> byId = informationRepository.findById(informationId);
        if (!byId.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.INFORMATION_NOT_EXIST.getCode(), languageType));
        }
        Information information = byId.get();

        Integer zanCount = information.getZan();


        UserMidInformation userMidInformation = userMidInformationRepoitory.findByUserIdAndInfomationIdAndType(userId, informationId,2);

        //没有阅读过该条资讯
        if (userMidInformation == null){

            if (type == 0){
                if (zanCount == null){
                    zanCount = 0;
                }else {
                    zanCount -= 1;
                }
            }else {
                if (zanCount == null){
                    zanCount = 1;
                }else {
                    zanCount += 1;
                }
            }

            if (zanCount < 1){
                zanCount = 0;
            }


            information.setZan(zanCount);
            information.setIsZan(1);

            userMidInformation = new UserMidInformation();
            userMidInformation.setUserId(userId);
            userMidInformation.setInfomationId(informationId);
            userMidInformation.setType(2);
            userMidInformation.setCreateTime(new Date());
            userMidInformationRepoitory.save(userMidInformation);

            informationRepository.save(information);
        }else {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.INFORMATION_ALREADY_ZAN.getCode(), languageType));
        }



        return ResultVOUtil.success(information);

    }
}
