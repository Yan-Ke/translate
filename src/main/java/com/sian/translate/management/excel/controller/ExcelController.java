package com.sian.translate.management.excel.controller;


import com.sian.translate.VO.ResultVO;
import com.sian.translate.management.excel.service.ExcelService;
import com.sian.translate.utlis.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequestMapping(value = "/manage/excel")
public class ExcelController {


    @Autowired
    ExcelService excelService;

    /****
     *  导出会员信息
     * @param isMember 不传默认-1全部 0非会员 1会员 2过期会员
     * @param param 昵称或电话
     * @param page 如果不传导出全部数据
     * @return
     */
    @RequestMapping(value = "/exportmemberUserInfo", produces = "application/json;charset=UTF-8")
    ResultVO exportmemberUserInfo(@RequestParam(value = "isMember", required = false, defaultValue = "-1") Integer isMember,
                                  @RequestParam(value = "param", required = false) String param,
                                  @RequestParam(value = "month", required = false, defaultValue = "0") Integer month,
                                  @RequestParam(value = "page", required = false, defaultValue = "-1") Integer page,
                                  @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                                  HttpServletResponse response, HttpSession session) {

        try {
            ResultVO resultVO = excelService.exportmemberUserInfo(isMember, response, month,page, size, param, session);
            if (resultVO.getCode() == 0) {
                return null;
            } else {
                return resultVO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error("导出失败--" + e.toString());
        }

    }

    /****
     *  导出词条信息
     * @param id 词条id
     * @param name  词条名称
     * @param page 如果不传导出全部数据
     * @return
     */
    @GetMapping(value = "/exportThesaurus", produces = "application/json;charset=UTF-8")
    ResultVO exportThesaurus(@RequestParam(value = "id", required = false) Integer id,
                             @RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "page", required = false, defaultValue = "-1") Integer page,
                             @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                             HttpServletResponse response,
                             HttpSession session) {

        try {
            ResultVO resultVO = excelService.exportThesaurus(id, name, response, page, size, session);
            if (resultVO.getCode() == 0) {
                return null;
            } else {
                return resultVO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error("导出失败--" + e.toString());
        }

    }


    /***
     * 导入词典信息
     * @param id 词典id
     * @param file
     * @return
     */
    @PostMapping(value = "/importDictionary", produces = "application/json;charset=UTF-8")
    public ResultVO importDictionary(@RequestParam(value = "id", required = false) Integer id, @RequestParam("file") MultipartFile file, HttpSession session) {

        try {
            return excelService.importDictionary(id, file, session);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultVOUtil.error(e.toString());

        }
    }


    /****
     * 导出会员购买流水
     * @param beginTime 支付时间 开始 时间搓
     * @param endTime 支付时间 结束 时间搓
     * @param orderNo  订单编号
     * @param nickName 会员昵称
     * @return
     */
    @GetMapping(value = "/exportMemberPayRecord", produces = "application/json;charset=UTF-8")
    ResultVO exportMemberPayRecord(@RequestParam(value = "beginTime", required = false) String beginTime,
                                   @RequestParam(value = "endTime", required = false) String endTime,
                                   @RequestParam(value = "orderNo", required = false) String orderNo,
                                   @RequestParam(value = "nickName", required = false) String nickName,
                                   @RequestParam(value = "month", required = false) Integer month,
                                   @RequestParam(value = "page", required = false, defaultValue = "-1") Integer page,
                                   @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                                   HttpServletResponse response,
                                   HttpSession session) {


        try {
            ResultVO resultVO = excelService.exportMemberPayRecord(beginTime, endTime, orderNo, nickName,month, page, size,response, session);
            if (resultVO.getCode() == 0) {
                return null;
            } else {
                return resultVO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error("导出失败--" + e.toString());
        }
    }


    /****
     * 导出财务统计报表
     * @param beginTime 支付时间 开始 时间搓
     * @param endTime 支付时间 结束 时间搓
     * @return
     */
    @GetMapping(value = "/exportFinancialInfo", produces = "application/json;charset=UTF-8")
    ResultVO exportFinancialInfo(@RequestParam(value = "beginTime", required = false) String beginTime,
                                   @RequestParam(value = "endTime", required = false) String endTime,
                                   @RequestParam(value = "page", required = false, defaultValue = "-1") Integer page,
                                   @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                                   HttpServletResponse response,
                                   HttpSession session) {
        try {
            ResultVO resultVO = excelService.exportFinancialInfo(beginTime, endTime,page, size,response, session);
            if (resultVO.getCode() == 0) {
                return null;
            } else {
                return resultVO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.error("导出失败--" + e.toString());
        }
    }
}
