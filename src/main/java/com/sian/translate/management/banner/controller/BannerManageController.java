package com.sian.translate.management.banner.controller;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.management.banner.service.BannerManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

/***
 * banner管理
 */
@RestController
@RequestMapping(value = "/manage/banner")
public class BannerManageController {

    @Autowired
    BannerManageService bannerManageService;

    /****
     *  新增banner
     * @param type 0汉语 1藏语
     * @return
     */
    @PostMapping(value = "/addBanner", produces = "application/json;charset=UTF-8")
    ResultVO addBanner(@RequestParam(value = "content", required = false) String content,
                       @RequestParam(value = "url", required = false) String url,
                       @RequestParam(value = "type", required = false) Integer type,
                       @RequestParam(value = "file", required = false) MultipartFile file,
                       HttpSession session) {
        return bannerManageService.addBanner(content, url, type,file,session);
    }

    /****
     *  修改banner
     * @param type 0汉语 1藏语
     * @return
     */

    @PostMapping(value = "/editBanner", produces = "application/json;charset=UTF-8")
    ResultVO editBanner(@RequestParam(value = "bannerId", required = false) Integer bannerId,
                        @RequestParam(value = "content", required = false) String content,
                       @RequestParam(value = "url", required = false) String url,
                       @RequestParam(value = "type", required = false) Integer type,
                       @RequestParam(value = "file", required = false) MultipartFile file,
                       HttpSession session) {
        return bannerManageService.editBanner(bannerId,content, url, type,file,session);
    }

    /****
     *  修改banner
     * @param type 0汉语 1藏语
     * @return
     */

    @PostMapping(value = "/deleteBanner", produces = "application/json;charset=UTF-8")
    ResultVO deleteBanner(@RequestParam(value = "bannerId", required = false) Integer bannerId,
                        HttpSession session) {
        return bannerManageService.deleteBanner(bannerId,session);
    }
}
