package com.sian.translate.dictionary.service.impl;

import com.sian.translate.DTO.PageInfoDto;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.dictionary.enity.Dictionary;
import com.sian.translate.dictionary.enity.UserCollectionDictionary;
import com.sian.translate.dictionary.enity.UserTranslateRecord;
import com.sian.translate.dictionary.repository.DictionaryRepository;
import com.sian.translate.dictionary.repository.UserCollectionDictionrayRepository;
import com.sian.translate.dictionary.repository.UserTranslateRecordRepository;
import com.sian.translate.dictionary.service.DictionaryService;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.member.enity.MemberPayRecord;
import com.sian.translate.utlis.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    DictionaryRepository dictionaryRepository;

    @Autowired
    HintMessageService hintMessageService;

    @Autowired
    UserTranslateRecordRepository userTranslateRecordRepository;

    @Autowired
    UserCollectionDictionrayRepository userCollectionDictionrayRepository;

    @Override
    public ResultVO translate(String languageType, Integer userID, Integer fromType, Integer toType, String content) {

        if (StringUtils.isEmpty(userID)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }
        if (StringUtils.isEmpty(content)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.CONTENT_NOT_EMPTY.getCode(), languageType));
        }

        Dictionary dictionary = new Dictionary();
        dictionary.setContent(content);

        if (fromType == toType) {
            return ResultVOUtil.success(dictionary);
        }
        /**需要翻译的语言类型 0 汉语 1 藏语 2 梵语 3 日语 4 英语**/
        switch (fromType) {
            case 0:
                dictionary = dictionaryRepository.findByChinese(content);
                break;
            case 1:
                dictionary = dictionaryRepository.findByZang(content);
                break;
            case 2:
                dictionary = dictionaryRepository.findBySanskirt(content);
                break;
            case 3:
                dictionary = dictionaryRepository.findByJapanese(content);
                break;
            case 4:
                dictionary = dictionaryRepository.findByEnglish(content);
                break;

        }
        String translateContent = "";
        switch (toType) {
            case 0:
                translateContent = dictionary.getChinese();
                break;
            case 1:
                translateContent = dictionary.getChinese();
                break;
            case 2:
                translateContent = dictionary.getSanskirt();
                break;
            case 3:
                translateContent = dictionary.getJapanese();
                break;
            case 4:
                translateContent = dictionary.getEnglish();
                break;

        }
        dictionary.setContent(translateContent);


        UserTranslateRecord userTranslateRecord = new UserTranslateRecord();
        userTranslateRecord.setUserId(userID);
        userTranslateRecord.setContent(content);
        userTranslateRecord.setFormType(fromType);
        userTranslateRecord.setToType(toType);
        userTranslateRecord.setTranslateContent(translateContent);
        userTranslateRecord.setCreateTime(new Date());

        userTranslateRecordRepository.save(userTranslateRecord);

        return ResultVOUtil.success(dictionary);
    }

    @Override
    public ResultVO getTranslateRecord(String languageType, Integer userId, int page, int size) {

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }

        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        if (page < 1){
            page = 1;
        }
        if (size < 1){
            size = 1;
        }
        Pageable pageable = PageRequest.of(page-1,size,sort);


        Page<UserTranslateRecord> datas = userTranslateRecordRepository.findByUserId(userId,pageable);

        int totalElements = (int) datas.getTotalElements(); //总条数
        int totalPages =  datas.getTotalPages(); // 总页数
        List<UserTranslateRecord> content = datas.getContent(); // 数据列表

        PageInfoDto pageInfoDto =  new PageInfoDto();
        pageInfoDto.setPage(page);
        pageInfoDto.setSize(size);
        pageInfoDto.setTotalElements(totalElements);
        pageInfoDto.setTotalPages(totalPages);
        pageInfoDto.setList(content);

        return  ResultVOUtil.success(content);
    }

    @Override
    public ResultVO getUserCollectionDictionary(String languageType, Integer userId, Integer page, Integer size) {
        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }

        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        if (page < 1){
            page = 1;
        }
        if (size < 1){
            size = 1;
        }
        Pageable pageable = PageRequest.of(page-1,size,sort);


        Page<UserCollectionDictionary> datas = userCollectionDictionrayRepository.findByUserId(userId,pageable);

        int totalElements = (int) datas.getTotalElements(); //总条数
        int totalPages =  datas.getTotalPages(); // 总页数
        List<UserCollectionDictionary> content = datas.getContent(); // 数据列表

        PageInfoDto pageInfoDto =  new PageInfoDto();
        pageInfoDto.setPage(page);
        pageInfoDto.setSize(size);
        pageInfoDto.setTotalElements(totalElements);
        pageInfoDto.setTotalPages(totalPages);
        pageInfoDto.setList(content);

        return  ResultVOUtil.success(pageInfoDto);
    }

    @Override
    public ResultVO collectionDictionary(String languageType, Integer userId, Integer fromType, Integer toType, String content, String translateContent) {

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }
        if (StringUtils.isEmpty(content)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.CONTENT_NOT_EMPTY.getCode(), languageType));
        }
        UserCollectionDictionary userCollectionDictionary = new UserCollectionDictionary();
        userCollectionDictionary.setUserId(userId);
        userCollectionDictionary.setContent(content);
        userCollectionDictionary.setTranslateContent(translateContent);
        userCollectionDictionary.setFormType(fromType);
        userCollectionDictionary.setToType(toType);
        userCollectionDictionary.setCreateTime(new Date());

        userCollectionDictionrayRepository.save(userCollectionDictionary);

        return ResultVOUtil.success(userCollectionDictionary);
    }

}
