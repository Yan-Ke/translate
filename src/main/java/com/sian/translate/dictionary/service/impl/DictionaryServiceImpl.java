package com.sian.translate.dictionary.service.impl;

import com.sian.translate.DTO.PageInfoDTO;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.dictionary.enity.Dictionary;
import com.sian.translate.dictionary.enity.Thesaurus;
import com.sian.translate.dictionary.enity.UserCollectionDictionary;
import com.sian.translate.dictionary.enity.UserTranslateRecord;
import com.sian.translate.dictionary.repository.DictionaryRepository;
import com.sian.translate.dictionary.repository.ThesaurusRepository;
import com.sian.translate.dictionary.repository.UserCollectionDictionrayRepository;
import com.sian.translate.dictionary.repository.UserTranslateRecordRepository;
import com.sian.translate.dictionary.service.DictionaryService;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.utlis.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
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

    @Autowired
    ThesaurusRepository thesaurusRepository;

    @Transactional
    @Override
    public ResultVO translate(String languageType, Integer userID, Integer type, String content) {

        if (StringUtils.isEmpty(userID)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }
        if (StringUtils.isEmpty(content)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.CONTENT_NOT_EMPTY.getCode(), languageType));
        }
        if(type == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.TRANSLATE_TYPE_EMPTY.getCode(), languageType));
        }


        Thesaurus thesaurus = thesaurusRepository.findFirstByContentOneAndType(content, type);
        if (thesaurus == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.TRANSLATE_RESULT_EMPTY.getCode(), languageType));
        }
        thesaurus.setTranslateResult(thesaurus.getContentTwo());

        UserTranslateRecord userTranslateRecord = new UserTranslateRecord();
        userTranslateRecord.setUserId(userID);
        userTranslateRecord.setContent(content);
        userTranslateRecord.setType(type);
        userTranslateRecord.setTranslateContent(thesaurus.getContentTwo());
        userTranslateRecord.setCreateTime(new Date());

        userTranslateRecordRepository.save(userTranslateRecord);

        return ResultVOUtil.success(thesaurus);
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

        PageInfoDTO pageInfoDto =  new PageInfoDTO();
        pageInfoDto.setPage(page);
        pageInfoDto.setSize(size);
        pageInfoDto.setTotalElements(totalElements);
        pageInfoDto.setTotalPages(totalPages);
        pageInfoDto.setList(content);

        return  ResultVOUtil.success(pageInfoDto);
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

        PageInfoDTO pageInfoDto =  new PageInfoDTO();
        pageInfoDto.setPage(page);
        pageInfoDto.setSize(size);
        pageInfoDto.setTotalElements(totalElements);
        pageInfoDto.setTotalPages(totalPages);
        pageInfoDto.setList(content);

        return  ResultVOUtil.success(pageInfoDto);
    }

    @Override
    public ResultVO collectionDictionary(String languageType, Integer userId, Integer type, String content, String translateContent) {

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
        userCollectionDictionary.setType(type);
        userCollectionDictionary.setCreateTime(new Date());

        userCollectionDictionrayRepository.save(userCollectionDictionary);

        return ResultVOUtil.success(userCollectionDictionary);
    }

    @Override
    public ResultVO getAllDictionary(String languageType, Integer userId) {

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }
        List<Dictionary> all = dictionaryRepository.findAll();

        return ResultVOUtil.success(all);
    }

}
