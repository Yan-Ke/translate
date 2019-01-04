package com.sian.translate.management.systemset.controller;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.management.systemset.service.SystemSetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * 新建通知
     * @param languageType 语言类型
     * @
     * @return
     */
    @PostMapping(value = "/addNotify", produces = "application/json;charset=UTF-8")
    ResultVO addNotify(@RequestParam(value = "languageType", required = false, defaultValue = "0") Integer languageType,
                       @RequestParam(value = "title", required = false) String title,
                       @RequestParam(value = "content", required = false) String content,
                       HttpSession session) {
        return systemSetService.addNotify(languageType, title, content, session);
    }

    /****
     * 删除通知
     * @param id 通知id
     * @return
     */
    @PostMapping(value = "/deleteNotify", produces = "application/json;charset=UTF-8")
    ResultVO deleteNotify(@RequestParam(value = "id", required = false) Integer id,
                          HttpSession session) {
        return systemSetService.deleteNotify(id, session);
    }


    /****
     * 获取通知列表
     * @param title 通知标题
     * @return
     */
    @GetMapping(value = "/getNotifykList", produces = "application/json;charset=UTF-8")
    ResultVO getNotifykList(@RequestParam(value = "title", required = false) String title,
                            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                            HttpSession session) {
        return systemSetService.getNotifykList(title, page, size, session);
    }


    /****
     * 获取帮助中心列表
     * @param title 通知标题
     * @return
     */
    @GetMapping(value = "/getHelpCenterList", produces = "application/json;charset=UTF-8")
    ResultVO getHelpCenterList(@RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                               @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                               HttpSession session) {
        return systemSetService.getHelpCenterList(title, page, size, session);
    }


    /****
     * 新建帮助
     * @
     * @return
     */
    @PostMapping(value = "/addHelpCenter", produces = "application/json;charset=UTF-8")
    ResultVO addHelpCenter(@RequestParam(value = "title", required = false) String title,
                           @RequestParam(value = "content", required = false) String content,
                           @RequestParam(value = "status", required = false) Integer status,

                           HttpSession session) {
        return systemSetService.addHelpCenter(title, content, status, session);
    }

    /****
     * 删除帮助
     * @param id 帮助id
     * @return
     */
    @PostMapping(value = "/deleteHelpCenter", produces = "application/json;charset=UTF-8")
    ResultVO deleteHelpCenter(@RequestParam(value = "id", required = false) Integer id,
                              HttpSession session) {
        return systemSetService.deleteHelpCenter(id, session);
    }


    /****
     * 新建注册协议
     * @
     * @return
     */
    @PostMapping(value = "/addRegstieProtocol", produces = "application/json;charset=UTF-8")
    ResultVO addRegstieProtocol(@RequestParam(value = "content", required = false) String content,
                                HttpSession session) {
        return systemSetService.addRegstieProtocol(content, session);
    }

    /****
     * 获取注册协议
     * @
     * @return
     */
    @GetMapping(value = "/getRegstieProtocol", produces = "application/json;charset=UTF-8")
    ResultVO getRegstieProtocol(@RequestParam(value = "content", required = false) String content,
                                HttpSession session) {
        return systemSetService.getRegstieProtocol(session);
    }

    /****
     * 新建关于我们
     * @
     * @return
     */
    @PostMapping(value = "/addAboutMe", produces = "application/json;charset=UTF-8")
    ResultVO addAboutMe(@RequestParam(value = "phone", required = false) String phone,
                        @RequestParam(value = "content", required = false) String content,
                        HttpSession session) {
        return systemSetService.addAboutMe(phone,content, session);
    }

    /****
     * 获取关于我们
     * @
     * @return
     */
    @GetMapping(value = "/getAboutMe", produces = "application/json;charset=UTF-8")
    ResultVO getAboutMe(HttpSession session) {
        return systemSetService.getAboutMe(session);
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


}
