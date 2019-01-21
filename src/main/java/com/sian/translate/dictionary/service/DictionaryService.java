package com.sian.translate.dictionary.service;

import com.sian.translate.VO.ResultVO;

import javax.servlet.http.HttpServletResponse;

public interface DictionaryService {


    ResultVO translate(String languageType, Integer userID, Integer type,String content,Integer dictionaryId);

    ResultVO getTranslateRecord(String languageType, Integer userId,int page,int size);

    ResultVO getUserCollectionDictionary(String languageType, Integer userId, Integer page, Integer size);

    ResultVO collectionDictionary(String languageType, Integer userId, Integer type,Integer dictionaryId,String content, String translateContent,Integer isWord);

    ResultVO getAllDictionary(String languageType, Integer type, Integer userId);

    ResultVO downThesaurus(Integer id, Integer userId, String languageType, HttpServletResponse response);
}
