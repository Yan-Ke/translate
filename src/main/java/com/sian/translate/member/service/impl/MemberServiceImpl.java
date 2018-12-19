package com.sian.translate.member.service.impl;

import com.sian.translate.DTO.PageInfoDto;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.member.enity.MemberConfig;
import com.sian.translate.member.enity.MemberPayRecord;
import com.sian.translate.member.repository.MemberConfigRepository;
import com.sian.translate.member.repository.MemberPayRecordRepository;
import com.sian.translate.member.service.MemberService;
import com.sian.translate.utlis.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberPayRecordRepository memberpayRecordRepository;

    @Autowired
    HintMessageService hintMessageService;

    @Autowired
    MemberConfigRepository memberConfigRepository;

    @Override
    public ResultVO getInformationList(String userID,String languageType,Integer page ,Integer size) {

        if (StringUtils.isEmpty(userID)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }

        Sort sort = new Sort(Sort.Direction.DESC, "payTime");
        if (page < 1){
            page = 1;
        }
        if (size < 1){
            size = 1;
        }
        Pageable pageable = PageRequest.of(page-1,size,sort);


        Page<MemberPayRecord> datas = memberpayRecordRepository.findByUserId(userID,pageable);

        int totalElements = (int) datas.getTotalElements(); //总条数
        int totalPages =  datas.getTotalPages(); // 总页数
        List<MemberPayRecord> content = datas.getContent(); // 数据列表

        PageInfoDto pageInfoDto =  new PageInfoDto();
        pageInfoDto.setPage(page);
        pageInfoDto.setSize(size);
        pageInfoDto.setTotalElements(totalElements);
        pageInfoDto.setTotalPages(totalPages);
        pageInfoDto.setList(content);

        return ResultVOUtil.success(pageInfoDto);

    }

    /****
     * 获取会员资费列表
     * @param languageType
     * @return
     */
    @Override
    public ResultVO getMemberConfigList(String languageType) {

        List<MemberConfig> memberConfigList = memberConfigRepository.getMemberConfig();

        if (!StringUtils.isEmpty(languageType) && languageType.equals("1")){
            memberConfigList.stream().forEach(m -> m.setExplain(m.getExplainZang()));
        }else{
            memberConfigList.stream().forEach(m -> m.setExplain(m.getExplainChinese()));
        }

        return ResultVOUtil.success(memberConfigList);
    }
}
