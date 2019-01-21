package com.sian.translate.management.templates.finance;


import com.sian.translate.DTO.FinancialInfoDTO;
import com.sian.translate.DTO.PageInfoDTO;
import com.sian.translate.DTO.UserRecordDTO;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.management.member.service.ManageMemberService;
import com.sian.translate.member.enity.MemberPayRecord;
import com.sian.translate.member.repository.MemberPayRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/manage")
public class FianceTemplatesController {

    @Autowired
    ManageMemberService manageMemberService;
    
    @Autowired
    MemberPayRecordRepository memberPayRecordRepository;


    @RequestMapping("/fiance/record")
    public ModelAndView record(@RequestParam(value = "beginTime", required = false,defaultValue = "")String beginTime,
                               @RequestParam(value = "endTime", required = false,defaultValue = "")String endTime,
                               @RequestParam(value = "orderNo", required = false,defaultValue = "")String orderNo,
                               @RequestParam(value = "nickName", required = false,defaultValue = "")String nickName,
                               @RequestParam(value = "month", required = false)Integer month,
                               @RequestParam(value = "page", required = false,defaultValue = "1")Integer page,
                               @RequestParam(value = "size", required = false,defaultValue = "20")Integer size,
                               HttpSession session, Map<String, Object> map) {

        ResultVO resultVO = manageMemberService.getAllMemberPayRecordList(beginTime, endTime, orderNo, nickName, month, page, size, session);
        PageInfoDTO pageInfoDTO = (PageInfoDTO) resultVO.getData();
       List<UserRecordDTO> userRecordDTOList = (List<UserRecordDTO>) pageInfoDTO.getList();

        BigDecimal totalAmount = BigDecimal.ZERO;

        if (userRecordDTOList.size() > 0){
            totalAmount = userRecordDTOList.get(0).getTotalAmount();
        }
        map.put("totalAmount", totalAmount);

        map.put("userRecordDTOList", userRecordDTOList);
        map.put("indexPage", pageInfoDTO.getPage());
        map.put("totalPage", pageInfoDTO.getTotalPages());
        map.put("totalElements", pageInfoDTO.getTotalElements());

        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        map.put("orderNo", orderNo);
        map.put("nickName", nickName);
        map.put("month", month);

        return new ModelAndView("html/finance/record.html", map);
    }

    @RequestMapping("/fiance/reportForm")
    public ModelAndView reportForm(@RequestParam(value = "beginTime", required = false,defaultValue = "")String beginTime,
                                   @RequestParam(value = "endTime", required = false,defaultValue = "")String endTime,
                                   @RequestParam(value = "page", required = false,defaultValue = "1")Integer page,
                                   @RequestParam(value = "size", required = false,defaultValue = "20")Integer size,
                                   HttpSession session, Map<String, Object> map) {

        ResultVO resultVO = manageMemberService.getAllFinancialInfo(beginTime, endTime, page, size, session);

        PageInfoDTO pageInfoDTO = (PageInfoDTO) resultVO.getData();
        List<FinancialInfoDTO> financialInfoDTOList = (List<FinancialInfoDTO>) pageInfoDTO.getList();

        map.put("financialInfoDTOList", financialInfoDTOList);
        map.put("indexPage", pageInfoDTO.getPage());
        map.put("totalPage", pageInfoDTO.getTotalPages());
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);


        return new ModelAndView("html/finance/report_form.html", map);
    }

    @RequestMapping("/fiance/details")
    public ModelAndView details(Integer id,String nickName,String couponName, Map<String, Object> map) {
        Optional<MemberPayRecord> byId = memberPayRecordRepository.findById(id);
        
        if (byId.isPresent()){
            MemberPayRecord memberPayRecord = byId.get();
            if (memberPayRecord.getReduceAmount() != null){
                memberPayRecord.setActualAmount(memberPayRecord.getAmount().add(memberPayRecord.getReduceAmount()));
            }else {
                memberPayRecord.setActualAmount(memberPayRecord.getAmount());
            }
            if (memberPayRecord.getMonth() != null){
                int month = memberPayRecord.getMonth();
                String monthString = "";
                if (month == 1){
                    monthString = "月";
                }else if (month == 3){
                    monthString = "季度";
                }else if (month == 6){
                    monthString = "半年";
                }else if (month == 12){
                    monthString = "年";
                }
                memberPayRecord.setMonthString(monthString);
                map.put("memberPayRecord", memberPayRecord);

            }

            map.put("memberPayRecord", memberPayRecord);

        }
        map.put("nickName", nickName);
        map.put("couponName", couponName);


        return new ModelAndView("html/finance/details.html", map);
    }


}
