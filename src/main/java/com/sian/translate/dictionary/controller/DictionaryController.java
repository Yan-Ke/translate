package com.sian.translate.dictionary.controller;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.dictionary.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    @Autowired
    DictionaryService dictionaryService;

    /****
     *  获取该词对应的翻译内容
     * @param languageType app语言
     * @param userId 用户id
     * @param type 词典类型 1藏汉 2藏英 3藏日 4藏梵
     * @param content 需要翻译的内容
     * @return
     */
    @GetMapping(value = "/translate", produces = "application/json;charset=UTF-8")
    ResultVO translate(@RequestParam(value = "languageType", required = false) String languageType,
                       @RequestParam(value = "userId", required = false) Integer userId,
                       @RequestParam(value = "type", required = false) Integer type,
                       @RequestParam(value = "content",required = false) String content) {
        return  dictionaryService.translate(languageType,userId,type,content);
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
                                @RequestParam(value = "page", required = false,defaultValue = "1")Integer page,
                                @RequestParam(value = "size", required = false,defaultValue = "20")Integer size) {
        return  dictionaryService.getTranslateRecord(languageType,userId,page,size);
    }


    /****
     *  收藏单词
     * @param languageType app语言
     * @param userId 用户id
     * @param type 词典类型 1藏汉 2藏英 3藏日 4藏梵
     * @param content 需要翻译的内容
     * @return
     */
    @PostMapping(value = "/collectionDictionary", produces = "application/json;charset=UTF-8")
    ResultVO collectionDictionary(@RequestParam(value = "languageType", required = false) String languageType,
                       @RequestParam(value = "userId", required = false) Integer userId,
                       @RequestParam(value = "type", required = false) Integer type,
                       @RequestParam(value = "content",required = false) String content,
                       @RequestParam(value = "translateContent",required = false) String translateContent) {
        return  dictionaryService.collectionDictionary(languageType,userId,type,content,translateContent);
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
                                @RequestParam(value = "page", required = false,defaultValue = "1")Integer page,
                                @RequestParam(value = "size", required = false,defaultValue = "20")Integer size) {
        return  dictionaryService.getUserCollectionDictionary(languageType,userId,page,size);
    }


    /****
     * 获取所有词典
     * @param languageType
     * @return
     */
    @GetMapping(value = "/getAllDictionary", produces = "application/json;charset=UTF-8")
    ResultVO getAllDictionary(@RequestParam(value = "languageType", required = false) String languageType,@RequestParam(value = "userId", required = false) Integer userId){
        return dictionaryService.getAllDictionary(languageType,userId);
    }



}
