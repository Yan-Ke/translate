package com.sian.translate.management.dictionary.service.impl;

import com.sian.translate.DTO.PageInfoDTO;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.dictionary.enity.Dictionary;
import com.sian.translate.dictionary.enity.Thesaurus;
import com.sian.translate.dictionary.repository.DictionaryRepository;
import com.sian.translate.dictionary.repository.ThesaurusRepository;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.management.dictionary.service.DictionaryManageService;
import com.sian.translate.management.user.service.ManageUserService;
import com.sian.translate.utlis.ImageUtlis;
import com.sian.translate.utlis.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DictionaryManageServiceImpl implements DictionaryManageService {

    @Autowired
    HintMessageService hintMessageService;

    @Autowired
    DictionaryRepository dictionaryRepository;

    @Autowired
    ThesaurusRepository thesaurusRepository;



    @Override
    public ResultVO addDictionaryr(String name, Integer type, Integer isMemberVisible, MultipartFile image, HttpSession session) {
        String languageType = "0";


        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        if (StringUtils.isEmpty(name)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.DICTIONARY_NAME_NOT_EMPTY.getCode(), languageType));
        }
        if (type == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.DICTIONARY_TYPE_NOT_EMPTY.getCode(), languageType));
        }
        if (isMemberVisible != 0){
            isMemberVisible = 1;
        }

        if (image == null || image.isEmpty()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.DICTIONARY_IMAGE_NOT_EMPTY.getCode(), languageType));
        }
        String imagePath = "";
        try {
            imagePath = ImageUtlis.loadImage(image);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.IMG_FORMAT_ERROR.getCode(), languageType));
        }

        Dictionary dictionary = new Dictionary();
        dictionary.setName(name);
        dictionary.setType(type);
        dictionary.setImage(imagePath);
        dictionary.setIsMemberVisible(isMemberVisible);
        dictionary.setUserId(userId);
        dictionary.setCreateTime(new Date());
        dictionary.setUpdateTime(new Date());
        dictionary.setUpdateUserId(userId);

        dictionaryRepository.save(dictionary);

        String logmsg = "新增词典:"+name;

        return ResultVOUtil.success(dictionary,logmsg);
    }

    @Override
    public ResultVO editDictionaryr(Integer id, String name, Integer type, Integer isMemberVisible, MultipartFile image, HttpSession session) {
        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }
        if (id == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.DICTIONARY_ID_NOT_EMPTY.getCode(), languageType));
        }

        Optional<Dictionary> dictionaryOptional = dictionaryRepository.findById(id);

        if (!dictionaryOptional.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.DICTIONARY_NOT_EXIST.getCode(), languageType));
        }

        Dictionary dictionary = dictionaryOptional.get();

        if (!StringUtils.isEmpty(name)){
            dictionary.setName(name);
        }
        if (type != null){
            dictionary.setType(type);
        }
        if (isMemberVisible == null){
            dictionary.setIsMemberVisible(isMemberVisible);
        }
        String imagePath = "";

        if (image != null && !image.isEmpty()){
            try {
                imagePath = ImageUtlis.loadImage(image);
            } catch (IOException e) {
                e.printStackTrace();
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.IMG_FORMAT_ERROR.getCode(), languageType));
            }
        }
        if (!StringUtils.isEmpty(imagePath)){
            dictionary.setImage(imagePath);
        }
        dictionary.setUpdateTime(new Date());
        dictionary.setUpdateUserId(userId);

        dictionaryRepository.save(dictionary);

        String logmsg = "修改词典:"+name;

        return ResultVOUtil.success(dictionary,logmsg);
    }

    @Override
    public ResultVO deleteDictionaryr(Integer id, HttpSession session) {
        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        if (id == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.DICTIONARY_ID_NOT_EMPTY.getCode(), languageType));
        }

        Optional<Dictionary> dictionaryOptional = dictionaryRepository.findById(id);
        if (!dictionaryOptional.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.DICTIONARY_NOT_EXIST.getCode(), languageType));
        }
        Dictionary dictionary = dictionaryOptional.get();
        dictionaryRepository.deleteById(id);

        String logmsg = "删除词典:"+dictionary.getName();

        return ResultVOUtil.success(logmsg);
    }

    @Override
    public ResultVO getAllDictionary(HttpSession session) {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }
        List<Dictionary> all = dictionaryRepository.findAll();

        return ResultVOUtil.success(all);
    }

    @Override
    public ResultVO getThesaurusList(Integer id, String name,Integer page, Integer size, HttpSession session) {

        String languageType = "0";


        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }
        if (id == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.DICTIONARY_ID_NOT_EMPTY.getCode(), languageType));
        }

        if (page < 1){
            page = 1;
        }
        if (size < 1){
            size = 1;
        }

        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");

        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Thesaurus> thesaurusPage ;
        if (StringUtils.isEmpty(name)){
            thesaurusPage = thesaurusRepository.findByDictionaryId(id, pageable);
        }else{
            name = "%" + name + "%";
            thesaurusPage = thesaurusRepository.findByDictionaryIdAndAndContentOneLike(id,name, pageable);
        }


        long totalElements = thesaurusPage.getTotalElements();
        int totalPages = thesaurusPage.getTotalPages();

        PageInfoDTO pageInfoDTO = new PageInfoDTO();

        pageInfoDTO.setTotalPages(totalPages);
        pageInfoDTO.setTotalElements((int)totalElements);
        pageInfoDTO.setPage(page);
        pageInfoDTO.setSize(size);
        pageInfoDTO.setList(thesaurusPage.getContent());

        return ResultVOUtil.success(pageInfoDTO);
    }

    @Override
    public ResultVO addThesaurus(Integer id, String contentOne, String contentTwo, HttpSession session) {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        if (id == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.DICTIONARY_ID_NOT_EMPTY.getCode(), languageType));
        }

        if (StringUtils.isEmpty(contentOne) || StringUtils.isEmpty(contentTwo)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.THESAURUS_NOT_EMPTY.getCode(), languageType));
        }
        Optional<Dictionary> dictionaryOptional = dictionaryRepository.findById(id);

        if (!dictionaryOptional.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.DICTIONARY_NOT_EXIST.getCode(), languageType));
        }

        Dictionary dictionary = dictionaryOptional.get();


        Thesaurus thesaurus = new Thesaurus();
        thesaurus.setContentOne(contentOne);
        thesaurus.setContentTwo(contentTwo);
        thesaurus.setType(dictionary.getType());
        thesaurus.setCreateUser(userId);
        thesaurus.setCreateTime(new Date());
        thesaurus.setDictionaryId(id);
        thesaurus.setUpdateUser(userId);
        thesaurus.setUpdateTime(new Date());

        thesaurusRepository.save(thesaurus);

        String logmsg = "新增词条"+"("+contentOne+"->"+contentTwo+")"+"至词典:"+dictionary.getName();

        return ResultVOUtil.success(thesaurus,logmsg);
    }

    @Override
    public ResultVO editThesaurus(Integer thesaurusId, String contentOne, String contentTwo, HttpSession session) {
        String languageType = "0";


        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }
        if (thesaurusId == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.THESAURUS_ID_NOT_EMPTY.getCode(), languageType));
        }



        Optional<Thesaurus> thesaurusOptional = thesaurusRepository.findById(thesaurusId);

        if (!thesaurusOptional.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.THESAURUS_NOT_EXIST.getCode(), languageType));
        }

        Thesaurus thesaurus = thesaurusOptional.get();

        if (!StringUtils.isEmpty(contentOne)){
            thesaurus.setContentOne(contentOne);
        }
        if (!StringUtils.isEmpty(contentTwo)){
            thesaurus.setContentTwo(contentTwo);
        }

        thesaurus.setUpdateUser(userId);
        thesaurus.setUpdateTime(new Date());
        thesaurusRepository.save(thesaurus);

        String logmsg = "修改词条"+"("+contentOne+"->"+contentTwo+")";


        return ResultVOUtil.success(thesaurus,logmsg);
    }

    @Override
    public ResultVO deleteThesaurus(Integer thesaurusId, HttpSession session) {
        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        if (thesaurusId == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.THESAURUS_ID_NOT_EMPTY.getCode(), languageType));
        }

        Optional<Thesaurus> thesaurusOptional = thesaurusRepository.findById(thesaurusId);
        if (!thesaurusOptional.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.THESAURUS_NOT_EXIST.getCode(), languageType));
        }
        Thesaurus thesaurus = thesaurusOptional.get();
        thesaurusRepository.deleteById(thesaurusId);

        String logmsg = "删除词条"+"("+thesaurus.getContentOne()+"->"+thesaurus.getContentTwo()+")";

        return ResultVOUtil.success(logmsg);
    }
}
