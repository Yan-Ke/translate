package com.sian.translate.member.service;

import com.sian.translate.VO.ResultVO;

public interface MemberService {


    /****
     * 获取资讯列表
     * @param userID 用户id
     * @param page  当前页码
     * @param size  条数
     * @return
     */
    ResultVO getInformationList(String userID, String languageType,Integer page, Integer size);

    /****
     * 获取会员资费列表
     * @param languageType
     * @return
     */
    ResultVO getMemberConfigList(String languageType);
}
