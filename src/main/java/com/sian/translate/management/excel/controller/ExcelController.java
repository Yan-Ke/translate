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
     * @param isMember 不传默认-1全部 0非会员 1会员
     * @param page 如果不传导出全部数据
     * @return
     */
    @GetMapping(value = "/exportmemberUserInfo", produces = "application/json;charset=UTF-8")
    ResultVO exportmemberUserInfo(@RequestParam(value = "isMember", required = false, defaultValue = "-1") Integer isMember,
                                  @RequestParam(value = "page", required = false, defaultValue = "-1") Integer page,
                                  @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                                  HttpServletResponse response, HttpSession session) {

        try {
            ResultVO resultVO = excelService.exportmemberUserInfo(isMember, response, page, size, session);
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
     * @param file
     * @return
     */
    @PostMapping(value = "/importDictionary", produces = "application/json;charset=UTF-8")
    public ResultVO importDictionary(@RequestParam("file") MultipartFile file,HttpSession session) {

        try {
            return excelService.importDictionary(file,session);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultVOUtil.error(e.toString());

        }
    }

}
