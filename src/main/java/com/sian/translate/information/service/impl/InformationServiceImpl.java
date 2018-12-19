package com.sian.translate.information.service.impl;

import com.sian.translate.DTO.PageInfoDto;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.information.enity.Information;
import com.sian.translate.information.repository.InformationRepository;
import com.sian.translate.information.service.InformationService;
import com.sian.translate.utlis.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class InformationServiceImpl implements InformationService {

    @Autowired
    InformationRepository informationRepository;

    @Override
    public ResultVO getInformationList(String languageType,Integer page ,Integer size) {

        int type = 0;
        if (!StringUtils.isEmpty(languageType) && languageType.equals("1")){
            type = 1;
        }
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        if (page < 1){
            page = 1;
        }
        if (size < 1){
            size = 1;
        }
        Pageable pageable = PageRequest.of(page-1,size,sort);


        Page<Information> datas = informationRepository.findAllByType(type,pageable);

        int totalElements = (int) datas.getTotalElements(); //总条数
        int totalPages =  datas.getTotalPages(); // 总页数
        List<Information> content = datas.getContent(); // 数据列表

        PageInfoDto pageInfoDto =  new PageInfoDto();
        pageInfoDto.setPage(page);
        pageInfoDto.setSize(size);
        pageInfoDto.setTotalElements(totalElements);
        pageInfoDto.setTotalPages(totalPages);
        pageInfoDto.setList(content);

        return ResultVOUtil.success(pageInfoDto);

    }
}
