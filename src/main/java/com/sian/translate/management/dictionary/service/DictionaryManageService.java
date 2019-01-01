package com.sian.translate.management.dictionary.service;

import com.sian.translate.VO.ResultVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

public interface DictionaryManageService {


    ResultVO addDictionaryr(String name, Integer type, Integer isMemberVisible, MultipartFile image, HttpSession session);

    ResultVO editDictionaryr(Integer id, String name, Integer type, Integer isMemberVisible, MultipartFile image, HttpSession session);

    ResultVO getAllDictionary(HttpSession session);

    ResultVO getThesaurusList(Integer id, String name,Integer page, Integer size, HttpSession session);

    ResultVO addThesaurus(Integer id, String contentOne, String contentTwo, HttpSession session);

    ResultVO editThesaurus(Integer thesaurusId, String contentOne, String contentTwo, HttpSession session);

    ResultVO deleteThesaurus(Integer thesaurusId, HttpSession session);

}
