package com.sian.translate.dictionary.service;

import com.sian.translate.VO.ResultVO;

public interface DictionaryService {


    ResultVO translate(String languageType, Integer userID, Integer fromType, Integer toType,String content);

    ResultVO getTranslateRecord(String languageType, Integer userId,int page,int size);

    ResultVO getUserCollectionDictionary(String languageType, Integer userId, Integer page, Integer size);

    ResultVO collectionDictionary(String languageType, Integer userId, Integer fromType, Integer toType, String content, String translateContent);
}
