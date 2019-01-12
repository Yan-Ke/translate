package com.sian.translate.management.systemset.controller;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.management.systemset.service.SystemSetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/manage/system")
public class SystemSetController {


    @Autowired
    SystemSetService systemSetService;


    /****
     * 获取用户反馈列表
     * @param status 0未处理 1 已处理
     * @param nickName 反馈人昵称
     * @param content 反馈内容
     * @return
     */
    @GetMapping(value = "/getFeedbackList", produces = "application/json;charset=UTF-8")
    ResultVO getFeedbackList(@RequestParam(value = "status", required = false, defaultValue = "0") Integer status,
                             @RequestParam(value = "nickName", required = false) String nickName,
                             @RequestParam(value = "content", required = false) String content,
                             @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                             @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                             HttpSession session) {
        return systemSetService.getFeedbackList(status, nickName, content, page, size, session);
    }

    /****
     * 处理用户反馈
     * @param id 反馈数据id
     * @return
     */
    @PostMapping(value = "/handleFeedback", produces = "application/json;charset=UTF-8")
    ResultVO handleFeedback(@RequestParam(value = "id", required = false) Integer id,
                            HttpSession session) {
        return systemSetService.handleFeedback(id, session);
    }

    /****
     * 获取文档列表
     * @param type 1关于我们 2注册协议3通知公告4帮助中心
     * @param field 文档名称
     * @return
     */
    @GetMapping(value = "/getFileList", produces = "application/json;charset=UTF-8")
    ResultVO getFileList(@RequestParam(value = "type", required = false, defaultValue = "0") Integer type,
                             @RequestParam(value = "field", required = false) String field,
                             @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                             @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                             HttpSession session) {
        return systemSetService.getFileList(type, field, page, size, session);
    }


    /****
     * 新增文档
     * @param type 1关于我们 2注册协议3通知公告4帮助中心
     * @param languageType 语言类型
     * @
     * @return
     */
    @PostMapping(value = "/addFile", produces = "application/json;charset=UTF-8")
    ResultVO addFile(@RequestParam(value = "type", required = false) Integer type,
                     @RequestParam(value = "languageType", required = false, defaultValue = "0") String languageType,
                     @RequestParam(value = "field", required = false) String field,
                     @RequestParam(value = "content", required = false) String content,
                     @RequestParam(value = "status", required = false, defaultValue = "0") Integer status,
                     @RequestParam(value = "order", required = false, defaultValue = "0") Integer order,
                     HttpSession session) {
        return systemSetService.addFile(type, languageType, field, content,status,order, session);
    }

    /****
     * 修改文档
     * @param type 1关于我们 2注册协议3通知公告4帮助中心
     * @param languageType 语言类型
     * @
     * @return
     */
    @PostMapping(value = "/editFile", produces = "application/json;charset=UTF-8")
    ResultVO editFile(@RequestParam(value = "id", required = false) Integer id,
                     @RequestParam(value = "type", required = false) Integer type,
                     @RequestParam(value = "languageType", required = false, defaultValue = "0") String languageType,
                     @RequestParam(value = "field", required = false) String field,
                     @RequestParam(value = "content", required = false) String content,
                     @RequestParam(value = "status", required = false, defaultValue = "0") Integer status,
                     @RequestParam(value = "order", required = false, defaultValue = "0") Integer order,
                     HttpSession session) {
        return systemSetService.editFile(id,type, languageType, field, content,status,order, session);
    }

    /****
     * 删除文档
     * @param id 通知id
     * @return
     */
    @PostMapping(value = "/deleteFile", produces = "application/json;charset=UTF-8")
    ResultVO deleteFile(@RequestParam(value = "id", required = false) Integer id,
                          HttpSession session) {
        return systemSetService.deleteFile(id, session);
    }


    /****
     * 获取用户翻译记录
     * @param type 1通过词条搜索 2用户昵称 3 无结果记录
     * @return
     */
    @GetMapping(value = "/getTranslateList", produces = "application/json;charset=UTF-8")
    ResultVO getTranslateList(@RequestParam(value = "type", required = false, defaultValue = "0") Integer type,
                              @RequestParam(value = "filed", required = false, defaultValue = "") String filed,
                              @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                              @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                              HttpSession session) {
        return systemSetService.getTranslateList(type,filed,page, size, session);
    }


    /****
     * 获取管理日志
     * @return
     */
    @GetMapping(value = "/getManageLogList", produces = "application/json;charset=UTF-8")
    ResultVO getManageLogList(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                              @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                              HttpSession session) {
        return systemSetService.getManageLogList(page, size, session);
    }

    /****
     *  上传图片
     * @return
     */
    @PostMapping(value = "/uploadImage", produces = "application/json;charset=UTF-8")
    ResultVO uploadImage(@RequestParam(value = "file", required = false) MultipartFile file) {
        return systemSetService.uploadImage(file);
    }

}
