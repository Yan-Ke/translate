package com.sian.translate.dictionary.controller;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.dictionary.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    @Autowired
    DictionaryService dictionaryService;

    /****
     *  获取该词对应的翻译内容
     * @param languageType app语言
     * @param userId 用户id
     * @param type 词典类型 1藏汉2 汉藏3 臧英 4英臧 5臧臧 6臧梵7 汉汉
     * @param content 需要翻译的内容
     * @return
     */
    @GetMapping(value = "/translate", produces = "application/json;charset=UTF-8")
    ResultVO translate(@RequestParam(value = "languageType", required = false) String languageType,
                       @RequestParam(value = "userId", required = false) Integer userId,
                       @RequestParam(value = "type", required = false) Integer type,
                       @RequestParam(value = "content", required = false) String content,
                       @RequestParam(value = "dictionaryId", required = false) Integer dictionaryId) {
        return dictionaryService.translate(languageType, userId, type, content,dictionaryId);
    }


    /*****
     * 获取历史输入记录
     * @param languageType
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/getTranslateRecord", produces = "application/json;charset=UTF-8")
    ResultVO getTranslateRecord(@RequestParam(value = "languageType", required = false) String languageType,
                                @RequestParam(value = "userId", required = false) Integer userId,
                                @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
        return dictionaryService.getTranslateRecord(languageType, userId, page, size);
    }


    /****
     *  收藏单词
     * @param languageType app语言
     * @param userId 用户id
     * @param type 词典类型 1藏汉2 汉藏3 臧英 4英臧 5臧臧 6臧梵7 汉汉
     * @param content 需要翻译的内容
     * @return
     */
    @PostMapping(value = "/collectionDictionary", produces = "application/json;charset=UTF-8")
    ResultVO collectionDictionary(@RequestParam(value = "languageType", required = false) String languageType,
                                  @RequestParam(value = "userId", required = false) Integer userId,
                                  @RequestParam(value = "type", required = false) Integer type,
                                  @RequestParam(value = "dictionaryId", required = false) Integer dictionaryId,
                                  @RequestParam(value = "content", required = false) String content,
                                  @RequestParam(value = "translateContent", required = false) String translateContent,
                                  @RequestParam(value = "isWord", required = false) Integer isWord) {

        return dictionaryService.collectionDictionary(languageType, userId, type, dictionaryId,content,translateContent,isWord);
    }

    /*****
     * 获取用户收藏单词记录
     * @param languageType
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/getUserCollectionDictionary", produces = "application/json;charset=UTF-8")
    ResultVO getUserCollectionDictionary(@RequestParam(value = "languageType", required = false) String languageType,
                                         @RequestParam(value = "userId", required = false) Integer userId,
                                         @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                         @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
        return dictionaryService.getUserCollectionDictionary(languageType, userId, page, size);
    }


    /****
     * 获取所有词典
     * @param languageType
     * @param type 0全部 1藏汉2 汉藏3 臧英 4英臧 5臧臧 6臧梵7 汉汉
     * @return
     */
    @GetMapping(value = "/getAllDictionary", produces = "application/json;charset=UTF-8")
    ResultVO getAllDictionary(@RequestParam(value = "languageType", required = false) String languageType,
                              @RequestParam(value = "type", required = false,defaultValue = "0") Integer type,
                              @RequestParam(value = "userId", required = false) Integer userId) {
        return dictionaryService.getAllDictionary(languageType,type, userId);
    }

    /****
     * 获取词典的所有记录(离线下载)
     * @param languageType
     * @return
     */
    @GetMapping(value = "/downThesaurus", produces = "application/json;charset=UTF-8")
    ResultVO downThesaurus(@RequestParam(value = "languageType", required = false) String languageType,
                           @RequestParam(value = "id", required = false,defaultValue = "0") Integer id,
                           @RequestParam(value = "userId", required = false) Integer userId, HttpServletResponse response) {

        return dictionaryService.downThesaurus(id,userId,languageType,response);

    }



}
