package com.sian.translate.management.templates.coupon;


import com.sian.translate.DTO.PageInfoDTO;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.coupon.enity.Coupon;
import com.sian.translate.coupon.enity.UserCouponRecord;
import com.sian.translate.coupon.repository.CouponRepository;
import com.sian.translate.management.coupon.service.CounponManageService;
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
@RequestMapping("/manage")
public class CouponTemplatesController {

    @Autowired
    CounponManageService counponManageService;

    @Autowired
    CouponRepository couponRepository;

    @RequestMapping("/coupon/list")
    public ModelAndView index(@RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                              @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                              HttpSession session, Map<String, Object> map) {


        ResultVO resultVO = counponManageService.getCouponList(name, page, size, session);

        PageInfoDTO pageInfoDTO = (PageInfoDTO) resultVO.getData();
        List<Coupon> couponList = (List<Coupon>) pageInfoDTO.getList();
        map.put("couponList", couponList);
        map.put("indexPage", pageInfoDTO.getPage());
        map.put("totalPage", pageInfoDTO.getTotalPages());
        if (!StringUtils.isEmpty(name)){
            map.put("name", name);
        }else{
            map.put("name", "");
        }


        return new ModelAndView("/html/coupon/index.html", map);
    }

    @RequestMapping("/coupon/grant")
    public ModelAndView grant(@RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                              @RequestParam(value = "size", required = false, defaultValue = "-1") Integer size,
                              @RequestParam(value = "isShow", required = false, defaultValue = "0") Integer isShow,
                              @RequestParam(value = "type", required = false, defaultValue = "0") Integer type,
                              @RequestParam(value = "memberType", required = false, defaultValue = "0") Integer memberType,
                              HttpSession session, Map<String, Object> map) {

        ResultVO resultVO = counponManageService.getCouponList(name, page, size, session);

        PageInfoDTO pageInfoDTO = (PageInfoDTO) resultVO.getData();
        List<Coupon> couponList = (List<Coupon>) pageInfoDTO.getList();
        map.put("couponList", couponList);
        map.put("indexPage", pageInfoDTO.getPage());
        map.put("totalPage", pageInfoDTO.getTotalPages());
        if (!StringUtils.isEmpty(name)){
            map.put("name", name);
        }else{
            map.put("name", "");
        }
        map.put("isShow", isShow);
        map.put("type", type);
        map.put("memberType", memberType);



        return new ModelAndView("/html/coupon/grant.html", map);
    }

    @RequestMapping("/coupon/add")
    public ModelAndView add(Map<String, Object> map) {

        return new ModelAndView("/html/coupon/add.html", map);
    }
    @RequestMapping("/coupon/edit")
    public ModelAndView edit(Integer id, Map<String, Object> map) {

        if (!StringUtils.isEmpty(id)) {
            Optional<Coupon> byId = couponRepository.findById(id);
            if (byId.isPresent()){
                Coupon coupon = byId.get();
                map.put("coupon", coupon);
            }
        }

        return new ModelAndView("/html/coupon/edit.html", map);
    }

    @RequestMapping("/coupon/record")
    public ModelAndView record(@RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                               @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                               HttpSession session,
                               Map<String, Object> map) {

        ResultVO resultVO = counponManageService.getCouponRecordList(name, page, size, session);

        PageInfoDTO pageInfoDTO = (PageInfoDTO) resultVO.getData();
        List<UserCouponRecord> userCouponRecordList = (List<UserCouponRecord>) pageInfoDTO.getList();
        map.put("userCouponRecordList", userCouponRecordList);
        map.put("indexPage", pageInfoDTO.getPage());
        map.put("totalPage", pageInfoDTO.getTotalPages());
        map.put("totalElements", pageInfoDTO.getTotalElements());

        if (!StringUtils.isEmpty(name)){
            map.put("name", name);
        }else{
            map.put("name", "");
        }

        return new ModelAndView("/html/coupon/record.html", map);
    }

}
