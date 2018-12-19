package com.sian.translate.dictionary.controller;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.dictionary.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    @Autowired
    DictionaryService dictionaryService;

    /****
     *  获取该词对应的翻译内容
     * @param languageType app语言
     * @param userId 用户id
     * @param fromType 需要翻译的语言类型 0 汉语 1 藏语 2 梵语 3 日语 4 英语
     * @param fromType 翻译后语类型 0 汉语 1 藏语 2 梵语 3 日语 4 英语
     * @param content 需要翻译的内容
     * @return
     */
    @GetMapping(value = "/translate", produces = "application/json;charset=UTF-8")
    ResultVO translate(@RequestParam(value = "languageType", required = false) String languageType,
                       @RequestParam(value = "userId", required = false) Integer userId,
                       @RequestParam(value = "fromType", required = false,defaultValue = "0") Integer fromType,
                       @RequestParam(value = "toType", required = false,defaultValue = "0") Integer toType,
                       @RequestParam(value = "content",required = false) String content) {
        return  dictionaryService.translate(languageType,userId,fromType,toType,content);
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
     * @param fromType 需要翻译的语言类型 0 汉语 1 藏语 2 梵语 3 日语 4 英语
     * @param fromType 翻译后语类型 0 汉语 1 藏语 2 梵语 3 日语 4 英语
     * @param content 需要翻译的内容
     * @return
     */
    @GetMapping(value = "/collectionDictionary", produces = "application/json;charset=UTF-8")
    ResultVO collectionDictionary(@RequestParam(value = "languageType", required = false) String languageType,
                       @RequestParam(value = "userId", required = false) Integer userId,
                       @RequestParam(value = "fromType", required = false,defaultValue = "0") Integer fromType,
                       @RequestParam(value = "toType", required = false,defaultValue = "0") Integer toType,
                       @RequestParam(value = "content",required = false) String content,
                       @RequestParam(value = "translateContent",required = false) String translateContent) {
        return  dictionaryService.collectionDictionary(languageType,userId,fromType,toType,content,translateContent);
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

}
