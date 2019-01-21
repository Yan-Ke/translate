package com.sian.translate.information.service;

import com.sian.translate.VO.ResultVO;

public interface InformationService {


    /****
     * 获取资讯列表
     * @param languageType 语言
     * @param page  当前页码
     * @param size  条数
     * @return
     */
    ResultVO getInformationList(Integer userId,String languageType,Integer type,Integer advLocation,Integer page ,Integer size);

    ResultVO readInformation(String languageType, Integer informationId, Integer userId);

    ResultVO zanInformation(String languageType, Integer informationId, Integer type, Integer userId);
}
