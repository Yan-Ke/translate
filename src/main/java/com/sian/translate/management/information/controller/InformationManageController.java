package com.sian.translate.management.information.controller;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.management.information.service.InformationManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

/***
 * 资讯管理
 */
@RestController
@RequestMapping(value = "/manage/information")
public class InformationManageController {

    @Autowired
    InformationManageService informationManageService;

    /****
     *  新增资讯
     * @param type 0汉语 1藏语
     * @return
     */
    @PostMapping(value = "/addInformation", produces = "application/json;charset=UTF-8")
    ResultVO addInformation(@RequestParam(value = "title", required = false) String title,
                            @RequestParam(value = "content", required = false) String content,
                            @RequestParam(value = "author", required = false) String author,
                            @RequestParam(value = "url", required = false) String url,
                            @RequestParam(value = "languageType", required = false) Integer languageType,
                            @RequestParam(value = "type", required = false) Integer type,
                            @RequestParam(value = "order", required = false) Integer order,
                            @RequestParam(value = "isShow", required = false) Integer isShow,
                            @RequestParam(value = "file", required = false) MultipartFile file,
                            HttpSession session) {
        return informationManageService.addInformation(title, content, author,url,languageType,type, order,isShow,file, session);
    }

    /****
     *  修改资讯
     * @param type 0汉语 1藏语
     * @return
     */

    @PostMapping(value = "/editInformotion", produces = "application/json;charset=UTF-8")
    ResultVO editInformotion(@RequestParam(value = "id", required = false) Integer informationId,
                             @RequestParam(value = "title", required = false) String title,
                             @RequestParam(value = "content", required = false) String content,
                             @RequestParam(value = "author", required = false) String author,
                             @RequestParam(value = "url", required = false) String url,
                             @RequestParam(value = "languageType", required = false) Integer languageType,
                             @RequestParam(value = "type", required = false) Integer type,
                             @RequestParam(value = "order", required = false) Integer order,
                             @RequestParam(value = "isShow", required = false) Integer isShow,
                             @RequestParam(value = "file", required = false) MultipartFile file,
                             HttpSession session) {
        return informationManageService.editInformotion(informationId, title, content, author,url,languageType,type, order,isShow,file, session);
    }

    /****
     *  删除资讯
     * @return
     */

    @RequestMapping(value = "/deleteInformotion", produces = "application/json;charset=UTF-8")
    ResultVO deleteInformotion(@RequestParam(value = "id", required = false) Integer informationId,
                               HttpSession session) {
        return informationManageService.deleteInformotion(informationId, session);
    }
}
