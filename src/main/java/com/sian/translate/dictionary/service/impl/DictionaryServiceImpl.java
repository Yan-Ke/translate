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
import com.sian.translate.management.excel.utils.ExportExcelUtils;
import com.sian.translate.user.entity.UserInfo;
import com.sian.translate.user.repository.UserInfoRepository;
import com.sian.translate.user.service.UserService;
import com.sian.translate.utlis.CommonUtlis;
import com.sian.translate.utlis.JsonUtil;
import com.sian.translate.utlis.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    UserInfoRepository userInfoRepository;
    @Autowired
    UserService userService;

    @Transactional
    @Override
    public ResultVO translate(String languageType, Integer userID, Integer type, String content,Integer dictionaryId) {

        if (StringUtils.isEmpty(userID)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }

        Optional<UserInfo> byId = userInfoRepository.findById(userID);

        if (!byId.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }

        if (StringUtils.isEmpty(content)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.CONTENT_NOT_EMPTY.getCode(), languageType));
        }
        if (type == null) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.TRANSLATE_TYPE_EMPTY.getCode(), languageType));
        }

        if (dictionaryId == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.DICTIONARY_ID_NOT_EMPTY.getCode(), languageType));
        }

        Thesaurus thesaurus = thesaurusRepository.findFirstByContentOneAndDictionaryId(content, dictionaryId);

        UserInfo userInfo = byId.get();


        UserTranslateRecord userTranslateRecord = new UserTranslateRecord();
        userTranslateRecord.setUserId(userID);
        userTranslateRecord.setContent(content);
        userTranslateRecord.setType(type);
        userTranslateRecord.setCreateTime(new Date());
        userTranslateRecord.setNickName(userInfo.getNickName());
        userTranslateRecord.setDictionaryId(dictionaryId);
        if (thesaurus == null) {
            userTranslateRecord.setTranslateContent(null);

            userTranslateRecordRepository.save(userTranslateRecord);
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.TRANSLATE_RESULT_EMPTY.getCode(), languageType));
        }

        String contentTwo = thesaurus.getContentTwo();
        thesaurus.setTranslateResult(contentTwo);

        userTranslateRecord.setTranslateContent(contentTwo);


        userTranslateRecordRepository.save(userTranslateRecord);

        return ResultVOUtil.success(thesaurus);
    }

    @Override
    public ResultVO getTranslateRecord(String languageType, Integer userId, int page, int size) {

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }

        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, size, sort);


        Page<UserTranslateRecord> datas = userTranslateRecordRepository.findByUserId(userId, pageable);

        int totalElements = (int) datas.getTotalElements(); //总条数
        int totalPages = datas.getTotalPages(); // 总页数
        List<UserTranslateRecord> content = datas.getContent(); // 数据列表

        PageInfoDTO pageInfoDto = new PageInfoDTO();
        pageInfoDto.setPage(page);
        pageInfoDto.setSize(size);
        pageInfoDto.setTotalElements(totalElements);
        pageInfoDto.setTotalPages(totalPages);
        pageInfoDto.setList(content);

        return ResultVOUtil.success(pageInfoDto);
    }

    @Override
    public ResultVO getUserCollectionDictionary(String languageType, Integer userId, Integer page, Integer size) {
        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }

        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, size, sort);


        Page<UserCollectionDictionary> datas = userCollectionDictionrayRepository.findByUserId(userId, pageable);

        int totalElements = (int) datas.getTotalElements(); //总条数
        int totalPages = datas.getTotalPages(); // 总页数
        List<UserCollectionDictionary> content = datas.getContent(); // 数据列表

        PageInfoDTO pageInfoDto = new PageInfoDTO();
        pageInfoDto.setPage(page);
        pageInfoDto.setSize(size);
        pageInfoDto.setTotalElements(totalElements);
        pageInfoDto.setTotalPages(totalPages);
        pageInfoDto.setList(content);

        return ResultVOUtil.success(pageInfoDto);
    }

    @Override
    public ResultVO collectionDictionary(String languageType, Integer userId, Integer type,Integer dictionaryId, String content, String translateContent,Integer isWord) {

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }
        if (StringUtils.isEmpty(content)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.CONTENT_NOT_EMPTY.getCode(), languageType));
        }
        if (dictionaryId == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.DICTIONARY_ID_NOT_EMPTY.getCode(), languageType));
        }
        if (isWord == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.IS_WORD_IS_EMPTY.getCode(), languageType));
        }



        UserCollectionDictionary userCollectionDictionary = new UserCollectionDictionary();
        userCollectionDictionary.setUserId(userId);
        userCollectionDictionary.setDictionaryId(dictionaryId);
        userCollectionDictionary.setContent(content);
        userCollectionDictionary.setTranslateContent(translateContent);
        userCollectionDictionary.setType(type);
        userCollectionDictionary.setCreateTime(new Date());
        userCollectionDictionary.setIsWord(isWord);
        userCollectionDictionrayRepository.save(userCollectionDictionary);

        return ResultVOUtil.success(userCollectionDictionary);
    }

    @Override
    public ResultVO getAllDictionary(String languageType, Integer type, Integer userId) {

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }


        List<Dictionary> all = dictionaryRepository.findAll(new Specification<Dictionary>() {
            @Override
            public Predicate toPredicate(Root<Dictionary> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();

                if (type != 0) {
                    predicates.add(criteriaBuilder.equal(root.get("type"), type));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });

        return ResultVOUtil.success(all);
    }

    @Override
    public ResultVO downThesaurus(Integer id, Integer userId, String languageType, HttpServletResponse response) {

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }

        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(userId);
        if (!userInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }

        if (id == null) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.DICTIONARY_ID_NOT_EMPTY.getCode(), languageType));
        }
        Optional<Dictionary> dictionaryOptional = dictionaryRepository.findById(id);

        if (!dictionaryOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.DICTIONARY_NOT_EXIST.getCode(), languageType));
        }

        UserInfo userInfo = userInfoOptional.get();

        boolean isMember = false;

        if (userInfo.getMemberBeginTime() != null
                && userInfo.getMemberEndTime() != null) {
            if (CommonUtlis.isEffectiveDate(new Date(), userInfo.getMemberBeginTime(), userInfo.getMemberEndTime())) {
                isMember = true;
            }
        }


        Dictionary dictionary = dictionaryOptional.get();
        Integer isMemberVisible = dictionary.getIsMemberVisible();
        if (isMemberVisible !=null && isMemberVisible == 1 && !isMember){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MEMBER_DOWN.getCode(), languageType));
        }

        List<Thesaurus> byDictionaryId = thesaurusRepository.findByDictionaryId(id);

        String result = JsonUtil.toJson(byDictionaryId);

        ExportExcelUtils.downloadTXT(response, dictionary.getName()+".json", result);

        return ResultVOUtil.success();
    }


}
