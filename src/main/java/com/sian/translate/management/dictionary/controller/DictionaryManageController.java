package com.sian.translate.management.dictionary.controller;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.management.dictionary.service.DictionaryManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/***
 * 词典词库关联
 */
@RestController
@RequestMapping(value = "/manage/dictionary")
public class DictionaryManageController {

    @Autowired
    DictionaryManageService dictionaryManageService;

    /****
     *  新增词典
     * @param name 词典名称
     * @param type 词典类型 1藏汉 2藏英 3藏日 4藏梵
     * @isMemberVisible 是否会员可见 0不是 1是
     * @return
     */
    @PostMapping(value = "/addDictionary", produces = "application/json;charset=UTF-8")
    ResultVO addDictionary(@RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "type", required = false) Integer type,
                           @RequestParam(value = "isMemberVisible", required = false, defaultValue = "0") Integer isMemberVisible,
                           @RequestParam(value = "image", required = false) String image,
                           HttpSession session,HttpServletRequest request) {
        return dictionaryManageService.addDictionaryr(name, type, isMemberVisible, image, session,request);
    }


    /****
     *  删除词典
     *
     * @param  id 词典id
     * @return
     */

    @PostMapping(value = "/deleteDictionaryr", produces = "application/json;charset=UTF-8")
    ResultVO deleteDictionaryr(@RequestParam(value = "id", required = false) Integer id,
                               HttpSession session) {
        return dictionaryManageService.deleteDictionaryr(id, session);
    }


    /****
     * 获取所有词典
     * @return
     */
    @GetMapping(value = "/getAllDictionary", produces = "application/json;charset=UTF-8")
    ResultVO getAllDictionary(HttpSession session) {
        return dictionaryManageService.getAllDictionary(session);
    }

    /****
     *  编辑词典
     * @param id 词典id
     * @param name 词典名称
     * @param type 词典类型 1藏汉 2藏英 3藏日 4藏梵
     * @isMemberVisible 是否会员可见 0不是 1是
     * @return
     */
    @PostMapping(value = "/editDictionaryr", produces = "application/json;charset=UTF-8")
    ResultVO editDictionaryr(@RequestParam(value = "id", required = false) Integer id,
                             @RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "type", required = false) Integer type,
                             @RequestParam(value = "isMemberVisible", required = false, defaultValue = "0") Integer isMemberVisible,
                             @RequestParam(value = "file", required = false) MultipartFile image,
                             HttpSession session, HttpServletRequest request) {
        return dictionaryManageService.editDictionaryr(id, name, type, isMemberVisible, image, session,request);
    }

    /****
     *  获取词典的词条列表
     * @param id 词典id
     * @param name 词条名称
     * @return
     */
    @GetMapping(value = "/getThesaurusList", produces = "application/json;charset=UTF-8")
    ResultVO getThesaurusList(@RequestParam(value = "id", required = false) Integer id,
                              @RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "page", required = false, defaultValue = "-1") Integer page,
                              @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                              HttpSession session) {
        return dictionaryManageService.getThesaurusList(id, name, page, size, session);
    }

    /****
     *  新增词条
     * @param id 词典id
     * @return
     */

    @PostMapping(value = "/addThesaurus", produces = "application/json;charset=UTF-8")
    ResultVO addThesaurus(@RequestParam(value = "id", required = false) Integer id,
                          @RequestParam(value = "contentOne", required = false) String contentOne,
                          @RequestParam(value = "contentTwo", required = false) String contentTwo,
                          @RequestParam(value = "image", required = false) String image,
                          HttpSession session) {
        return dictionaryManageService.addThesaurus(id, contentOne, contentTwo,image, session);
    }


    /****
     *  修改词条
     *
     * @param  thesaurusId 词条id
     * @return
     */

    @PostMapping(value = "/editThesaurus", produces = "application/json;charset=UTF-8")
    ResultVO editThesaurus(@RequestParam(value = "thesaurusId", required = false) Integer thesaurusId,
                           @RequestParam(value = "contentOne", required = false) String contentOne,
                           @RequestParam(value = "contentTwo", required = false) String contentTwo,
                           HttpSession session) {
        return dictionaryManageService.editThesaurus(thesaurusId, contentOne, contentTwo, session);
    }

    /****
     *  删除词条
     *
     * @param  thesaurusId 词条id
     * @return
     */

    @PostMapping(value = "/deleteThesaurus", produces = "application/json;charset=UTF-8")
    ResultVO deleteThesaurus(@RequestParam(value = "thesaurusId", required = false) Integer thesaurusId,
                             HttpSession session) {
        return dictionaryManageService.deleteThesaurus(thesaurusId, session);
    }


    /****
     *获取最新的5条词条信息
     * @return
     */

    @PostMapping(value = "/getNewThesaurus", produces = "application/json;charset=UTF-8")
    ResultVO getNewThesaurus() {
        return dictionaryManageService.getNewThesaurus();
    }
}
