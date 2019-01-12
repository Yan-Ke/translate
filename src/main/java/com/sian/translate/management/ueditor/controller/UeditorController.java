package com.sian.translate.management.ueditor.controller;

import com.alibaba.fastjson.JSON;
import com.sian.translate.management.ueditor.enity.Ueditor;
import com.sian.translate.utlis.CommonUtlis;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class UeditorController {

    @RequestMapping(value = "/ueditor")
    @ResponseBody
    public String ueditor(@RequestParam(value = "action",required = false) String param, MultipartFile upfile, HttpServletRequest request) {
        Ueditor ueditor = new Ueditor();
        if (StringUtils.isEmpty(param)){
            param = "uploadimage";
        }

        if (param != null && param.equals("config")){
            return "上传失败";
        }else if (param != null&& param.equals("uploadimage") || param.equals("uploadscrawl")){
            if (upfile != null) {
                //{state：”数据状态信息”，url：”图片回显路径”，title：”文件title”，original：”文件名称”，···}
                try {
                    return CommonUtlis.uploadImg(upfile, request);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    ueditor.setState("出现异常");
                    return JSON.toJSONString(ueditor);
                }
            } else {
                ueditor.setState("文件为空");
                return JSON.toJSONString(ueditor);
            }
        }else{
            ueditor.setState("不支持该操作");
            return JSON.toJSONString(ueditor);
        }
    }



    @RequestMapping(value = "/imgUpload")
    @ResponseBody
    public Ueditor imgUpload(@RequestParam(value = "action",required = false)  String param, MultipartFile upfile, HttpServletRequest request) {
        Ueditor ueditor = new Ueditor();
        return ueditor;
    }




}