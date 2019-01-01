package com.sian.translate.management.member.controller;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.management.member.service.ManageMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/manage/member")
public class ManageUserMember {


    @Autowired
    ManageMemberService manageMemberService;


    /****
     * 获取会员列表
     * @param isMember 不传默认-1全部 0非会员 1会员 2过期会员
     * @return
     */
    @GetMapping(value = "/getMemberList", produces = "application/json;charset=UTF-8")
    ResultVO getMemberList(@RequestParam(value = "isMember", required = false,defaultValue = "-1") Integer isMember,
                           @RequestParam(value = "param", required = false)String param,
                           @RequestParam(value = "page", required = false,defaultValue = "1")Integer page,
                           @RequestParam(value = "size", required = false,defaultValue = "20")Integer size,
                           HttpSession session) {
        return  manageMemberService.getMemberList(isMember,param,page,size,session);

    }

    /****
     * 增加会员资费配置
     * @param explainChinese  会员资费说明
     * @param monthAmount 月资费
     * @param quarterAmount 季度资费
     * @param halfYearAmount 半年资费
     * @param yearAmount 年度资费
     * @return
     */
    @PostMapping(value = "/addMemberConfig", produces = "application/json;charset=UTF-8")
    ResultVO addMemberConfig(@RequestParam(value = "explainChinese", required = false) String explainChinese,
                           @RequestParam(value = "explainZang", required = false)String explainZang,
                           @RequestParam(value = "monthAmount", required = false)String monthAmount,
                             @RequestParam(value = "quarterAmount", required = false)String quarterAmount,
                             @RequestParam(value = "halfYearAmount", required = false)String halfYearAmount,
                             @RequestParam(value = "yearAmount", required = false)String yearAmount,
                             HttpSession session) {
        return  manageMemberService.addMemberConfig(explainChinese,explainZang,monthAmount,quarterAmount,halfYearAmount,yearAmount,session);

    }


    /****
     * 修改会员资费配置
     * @param id 需要修改配置的id
     * @param explainChinese  会员资费说明中文
     * @param explainZang 会员资费说明藏语
     * @param amount 资费金额
     * @param month 会员月数
     * @return
     */
    @PostMapping(value = "/editMemberConfig", produces = "application/json;charset=UTF-8")
    ResultVO editMemberConfig(@RequestParam(value = "id", required = false) Integer id,
                              @RequestParam(value = "explainChinese", required = false) String explainChinese,
                             @RequestParam(value = "explainZang", required = false)String explainZang,
                             @RequestParam(value = "amount", required = false)String amount,
                             @RequestParam(value = "month", required = false)Integer month,
                             HttpSession session) {
        return  manageMemberService.editMemberConfig(id,explainChinese,explainZang,amount,month,session);
    }

    /****
     * 获取所有会员购买记录
     * @param beginTime 支付时间 开始 时间搓
     * @param endTime 支付时间 结束 时间搓
     * @param orderNo  订单编号
     * @param nickName 会员昵称
     * @return
     */
    @GetMapping(value = "/getAllMemberPayRecordList", produces = "application/json;charset=UTF-8")
    ResultVO getAllMemberPayRecordList(@RequestParam(value = "beginTime", required = false)String beginTime,
                                       @RequestParam(value = "endTime", required = false)String endTime,
                                       @RequestParam(value = "orderNo", required = false)String orderNo,
                                       @RequestParam(value = "nickName", required = false)String nickName,
                                       @RequestParam(value = "page", required = false,defaultValue = "1")Integer page,
                                    @RequestParam(value = "size", required = false,defaultValue = "20")Integer size,
                                    HttpSession session) {
        return  manageMemberService.getAllMemberPayRecordList(beginTime,endTime,orderNo,nickName,page,size,session);
    }


    /****
     * 获取财务统计报表
     * @param beginTime 支付时间 开始 时间搓
     * @param endTime 支付时间 结束 时间搓
     * @return
     */
    @GetMapping(value = "/getAllFinancialInfo", produces = "application/json;charset=UTF-8")
    ResultVO getAllFinancialInfo(@RequestParam(value = "beginTime", required = false)String beginTime,
                                       @RequestParam(value = "endTime", required = false)String endTime,
                                       @RequestParam(value = "page", required = false,defaultValue = "1")Integer page,
                                       @RequestParam(value = "size", required = false,defaultValue = "20")Integer size,
                                       HttpSession session) {
        return  manageMemberService.getAllFinancialInfo(beginTime,endTime,page,size,session);
    }



}
