package com.sian.translate.management.templates.information;


import com.sian.translate.DTO.PageInfoDTO;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.information.enity.Information;
import com.sian.translate.information.repository.InformationRepository;
import com.sian.translate.management.information.service.InformationManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(value = "/manage")
public class InfomationController {

    @Autowired
    InformationManageService informationManageService;

    @Autowired
    InformationRepository informationRepository;

    @RequestMapping("/information/list")
    public ModelAndView list(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                              @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                              @RequestParam(value = "title", required = false) String title,
                              HttpSession session, Map<String, Object> map) {

        ResultVO informotionList = informationManageService.getInformotionList(title, page, size, session);
        PageInfoDTO pageInfoDTO = (PageInfoDTO) informotionList.getData();
        List<Information> informationList = (List<Information>) pageInfoDTO.getList();
        map.put("informationList", informationList);
        map.put("indexPage", pageInfoDTO.getPage());
        map.put("totalPage", pageInfoDTO.getTotalPages());
        if (!StringUtils.isEmpty(title)){
            map.put("title", title);
        }

        return new ModelAndView("/html/information/index.html", map);
    }

    @RequestMapping("/information/add")
    public ModelAndView add(Map<String, Object> map) {

        return new ModelAndView("/html/information/add.html", map);
    }

    @RequestMapping("/information/detail")
    public ModelAndView deatil(Integer id, Map<String, Object> map) {

        Optional<Information> byId = informationRepository.findById(id);
        if (byId.isPresent()){
            map.put("information", byId.get());
        }

        return new ModelAndView("/html/information/detail.html", map);
    }
}
