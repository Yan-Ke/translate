package com.sian.translate.dictionary.service;

import com.sian.translate.VO.ResultVO;

public interface DictionaryService {


    ResultVO translate(String languageType, Integer userID, Integer type,String content);

    ResultVO getTranslateRecord(String languageType, Integer userId,int page,int size);

    ResultVO getUserCollectionDictionary(String languageType, Integer userId, Integer page, Integer size);

    ResultVO collectionDictionary(String languageType, Integer userId, Integer type, String content, String translateContent);

    ResultVO getAllDictionary(String languageType, Integer type, Integer userId);
}
