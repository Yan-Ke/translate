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
    ResultVO getInformationList(String languageType,Integer page ,Integer size);

}
