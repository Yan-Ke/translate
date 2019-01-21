package com.sian.translate.management.templates.member;


import com.sian.translate.DTO.PageInfoDTO;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.information.repository.InformationRepository;
import com.sian.translate.management.member.service.ManageMemberService;
import com.sian.translate.member.enity.MemberConfig;
import com.sian.translate.member.service.MemberService;
import com.sian.translate.user.entity.UserInfo;
import com.sian.translate.user.repository.UserInfoRepository;
import com.sian.translate.user.service.impl.UserServiceImpl;
import com.sian.translate.utlis.CommonUtlis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/manage")
public class MemberTemplatesController {

    @Autowired
    ManageMemberService manageMemberService;

    @Autowired
    InformationRepository informationRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    MemberService memberService;

    /****
     * 获取会员列表
     * @param isMember 不传默认-1全部 0非会员 1会员 2过期会员
     * @return
     */
    @RequestMapping("/member/list")
    public ModelAndView list(@RequestParam(value = "isMember", required = false, defaultValue = "-1") Integer isMember,
                             @RequestParam(value = "paramName", required = false) String paramName,
                             @RequestParam(value = "month", required = false, defaultValue = "0") Integer month,
                             @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                             @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                             HttpSession session, Map<String, Object> map) {

        ResultVO informotionList = manageMemberService.getMemberList(isMember, paramName,month, page, size, session);
        PageInfoDTO pageInfoDTO = (PageInfoDTO) informotionList.getData();
        List<UserInfo> userInfoList = (List<UserInfo>) pageInfoDTO.getList();
        map.put("userInfoList", userInfoList);
        map.put("indexPage", pageInfoDTO.getPage());
        map.put("totalPage", pageInfoDTO.getTotalPages());
        if (!StringUtils.isEmpty(paramName)) {
            map.put("paramName", paramName);
        }
        userInfoList.stream().forEach(userInfo -> setUserInfoDate(userInfo));

        map.put("isMember", isMember);
        map.put("month", month);


        return new ModelAndView("html/member/index.html", map);
    }


    @RequestMapping("/member/set")
    public ModelAndView deatil(Map<String, Object> map) throws ParseException {

        ResultVO resultVO = memberService.getMemberConfigList("0");
        List<MemberConfig> memberConfigList = (List<MemberConfig>) resultVO.getData();

        for (MemberConfig memberConfig : memberConfigList) {
            if (memberConfig.getMonth() == 1){
                map.put("monthAmount", memberConfig.getAmount());
            }else if (memberConfig.getMonth() == 3){
                map.put("quarterAmount", memberConfig.getAmount());
            }else if (memberConfig.getMonth() == 6){
                map.put("halfYearAmount", memberConfig.getAmount());
            }else if (memberConfig.getMonth() == 12){
                map.put("yearAmount", memberConfig.getAmount());
            }
            map.put("content", memberConfig.getExplainChinese());

        }


        return new ModelAndView("html/member/set.html", map);
    }

    @RequestMapping("/member/detail")
    public ModelAndView set(Integer id, Map<String, Object> map) throws ParseException {


        ResultVO resultVO = userService.getUserinfo(id, "0");
        UserInfo userinfo = (UserInfo) resultVO.getData();

        setUserInfoDate(userinfo);

        map.put("userInfo", userinfo);

        return new ModelAndView("html/member/details.html", map);
    }



    private void setUserInfoDate(UserInfo userinfo) {

        if (userinfo.getMemberBeginTime() != null
                && userinfo.getMemberEndTime() != null) {
            if (CommonUtlis.isEffectiveDate(new Date(), userinfo.getMemberBeginTime(), userinfo.getMemberEndTime())) {
                userinfo.setIsMember(1);
                userinfo.setVipIcon(CommonUtlis.VIPICON1);
            } else {
                userinfo.setVipIcon(CommonUtlis.VIPICON2);
                userinfo.setIsMember(2);
            }
        } else {
            userinfo.setVipIcon(CommonUtlis.VIPICON0);
            userinfo.setIsMember(0);
        }

        /**用户性别 0未知1男2女3保密**/
        if(userinfo.getSex() == null || userinfo.getSex() == 0){
            userinfo.setSexString("未知");
        }else if (userinfo.getSex() == 1){
            userinfo.setSexString("男");
        }else if (userinfo.getSex() == 2){
            userinfo.setSexString("女");
        }else if (userinfo.getSex() == 3){
            userinfo.setSexString("保密");
        }
        Date registrationTime = userinfo.getRegistrationTime();
        Date memberBeginTime = userinfo.getMemberBeginTime();
        Date memberEndTime = userinfo.getMemberEndTime();
        Date buyTime = userinfo.getBuyTime();
        Date loginTime = userinfo.getLoginTime();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String registrationTimeString = "";
        String memberBeginTimeString = "";
        String memberEndTimeString = "";
        String buyTimeTimeString = "";
        String loginTimeString = "";


        if (registrationTime != null) {
            registrationTimeString = format.format(registrationTime);
        }

        if (memberBeginTime != null) {
            memberBeginTimeString = format.format(memberBeginTime);
        }
        if (memberEndTime != null) {
            memberEndTimeString = format.format(memberEndTime);
        }
        if (buyTime != null) {
            buyTimeTimeString = format.format(buyTime);
        }
        if (loginTime != null) {
            loginTimeString = format.format(loginTime);
        }


        userinfo.setRegistrationTimeString(registrationTimeString);
        userinfo.setMemberBeginTimeString(memberBeginTimeString);
        userinfo.setMemberEndTimeString(memberEndTimeString);
        userinfo.setBuyTimeString(buyTimeTimeString);
        userinfo.setLoginTimeString(loginTimeString);
    }
}
