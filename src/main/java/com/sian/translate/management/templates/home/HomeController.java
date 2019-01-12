package com.sian.translate.management.templates.home;


import com.sian.translate.DTO.PageInfoDTO;
import com.sian.translate.DTO.StatisticalCountDTO;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.dictionary.enity.Thesaurus;
import com.sian.translate.information.enity.Information;
import com.sian.translate.management.dictionary.service.DictionaryManageService;
import com.sian.translate.management.information.service.InformationManageService;
import com.sian.translate.management.statistical.service.StatisticalService;
import com.sian.translate.management.user.entity.ManageUserInfo;
import com.sian.translate.management.user.service.ManageUserService;
import com.sian.translate.utlis.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/manage")
public class HomeController {

    @Autowired
    StatisticalService statisticalService;

    @Autowired
    ManageUserService manageUserService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    InformationManageService informationManageService;

    @Autowired
    DictionaryManageService dictionaryManageService;

    @RequestMapping("/index")
    public ModelAndView index(HttpSession session, Map<String, Object> map) {

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (!StringUtils.isEmpty(userId)) {

            String result = stringRedisTemplate.opsForValue().get("user_"+userId);

            ManageUserInfo userInfo;
            if (!StringUtils.isEmpty(result)){
                userInfo = (ManageUserInfo) JsonUtil.fromJson(result, ManageUserInfo.class);
            }else{
                ResultVO manageUserInfo = manageUserService.getManageUserInfo(userId, session);
                userInfo = (ManageUserInfo)manageUserInfo.getData();
            }
            map.put("user",userInfo);

        }
        return new ModelAndView("/html/index.html", map);
    }

    @RequestMapping("/home")
    public ModelAndView home(HttpSession session, Map<String, Object> map) {

        ResultVO resultVO = statisticalService.getTotalStatistical(session);
        StatisticalCountDTO statisticalCountDTO = (StatisticalCountDTO) resultVO.getData();

        ResultVO informotionResultVo = informationManageService.getInformotionList("", 1, 5, session);
        PageInfoDTO pageInfoDTO = (PageInfoDTO) informotionResultVo.getData();
         List<Information> informationList = (List<Information>) pageInfoDTO.getList();

        informationList.stream().forEach(information -> formatCreateTime(information));

        ResultVO newThesaurus = dictionaryManageService.getNewThesaurus();
       List<Thesaurus> thesaurusList = (List<Thesaurus>) newThesaurus.getData();

        map.put("statisticalCountDTO", statisticalCountDTO);
        map.put("informationList", informationList);
        map.put("thesaurusList", thesaurusList);



        return new ModelAndView("/html/index/home.html", map);
    }

    private void formatCreateTime(Information information) {

        //yyyy-MM-dd HH:mm
        Date createTime = information.getCreateTime();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String format1 = format.format(createTime);

        information.setCreateTimeString(format1);
    }

}
