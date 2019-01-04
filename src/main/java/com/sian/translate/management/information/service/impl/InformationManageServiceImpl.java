package com.sian.translate.management.information.service.impl;

import com.sian.translate.DTO.PageInfoDTO;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.information.enity.Information;
import com.sian.translate.information.repository.InformationRepository;
import com.sian.translate.management.information.service.InformationManageService;
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
import java.util.Optional;

@Service
public class InformationManageServiceImpl implements InformationManageService {

    @Autowired
    HintMessageService hintMessageService;

    @Autowired
    InformationRepository informationRepository;


    @Override
    public ResultVO addInformation(String title, String content, String author, String url,Integer languageType,Integer type, Integer order, Integer isShow, MultipartFile file, HttpSession session) {
        String languageTypeString = "0";


        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageTypeString));
        }

        if (languageType == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.INFORMATION_LANGUAGETYPE_NOT_EMPTY.getCode(), languageTypeString));
        }

        if (StringUtils.isEmpty(title)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.INFORMATION_TITLE_NOT_EMPTY.getCode(), languageTypeString));
        }

        if (type == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.INFORMATION_TYPE_NOT_EMPTY.getCode(), languageTypeString));
        }
        if (StringUtils.isEmpty(author)){
            author = "藏语翻译宝";
        }

        if (StringUtils.isEmpty(url) && StringUtils.isEmpty(content)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.INFORMATION_CONTENT_URL_NOT_EMPTY.getCode(), languageTypeString));
        }

        if (file == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.IMG_NOT_EMPTY.getCode(), languageTypeString));
        }
        String imagePath = "";
        try {
            imagePath = ImageUtlis.loadImage(file);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.IMG_FORMAT_ERROR.getCode(), languageTypeString));
        }

        Information information = new Information();
        information.setLanguageType(languageType);
        information.setTitle(title);
        information.setType(type);
        information.setAuthor(author);
        if (!StringUtils.isEmpty(url)){

            if (!url.startsWith("http://")){
                url = "http://"+url;
            }
            information.setUrl(url);
        }
        if (!StringUtils.isEmpty(content)){
            information.setContent(content);
        }
        information.setImage(imagePath);
        information.setCreateUser(userId);
        information.setCreateTime(new Date());
        information.setUpdateUser(userId);
        information.setUpdateTime(new Date());
        information.setInfomationOrder(order);
        information.setIsShow(isShow);

        informationRepository.save(information);

        String losmsg = "新增资讯:"+information.getTitle();
        return ResultVOUtil.success(information,losmsg);
    }

    @Override
    public ResultVO editInformotion(Integer informationId, String title, String content, String author, String url,Integer languageType,Integer type, Integer order, Integer isShow, MultipartFile file, HttpSession session) {
        String languageTypeString = "0";


        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageTypeString));
        }
        if (informationId == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.INFORMATION_ID_NOT_EMPTY.getCode(), languageTypeString));
        }

        /**
         * 输入了内容必须传入语言类型
         */
        if (!StringUtils.isEmpty(content) && type == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.INFORMATION_LANGUAGETYPE_NOT_EMPTY.getCode(), languageTypeString));
        }

        String imagePath = "";

        if (file != null){
            try {
                imagePath = ImageUtlis.loadImage(file);
            } catch (IOException e) {
                e.printStackTrace();
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.IMG_FORMAT_ERROR.getCode(), languageTypeString));
            }
        }

        Optional<Information> informationOptional = informationRepository.findById(informationId);

        if (!informationOptional.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.INFORMATION_NOT_EXIST.getCode(), languageTypeString));
        }


        Information information = informationOptional.get();

        if (languageType != null){
            information.setLanguageType(languageType);
        }
        if (!StringUtils.isEmpty(title)){
            information.setTitle(title);
        }
        if (type != null){
            information.setType(type);
        }

        if (!StringUtils.isEmpty(author)){
            information.setAuthor(author);
        }
        if (!StringUtils.isEmpty(author)){
            information.setAuthor(author);
        }


        if (!StringUtils.isEmpty(url)){

            if (!url.startsWith("http://")){
                url = "http://"+url;
            }
            information.setUrl(url);
        }
        if (!StringUtils.isEmpty(content)){
            information.setContent(content);
        }
        if (!StringUtils.isEmpty(imagePath)){
            information.setImage(imagePath);
        }

        if (order != null){
            information.setInfomationOrder(order);
        }
        if (isShow != null){
            information.setIsShow(isShow);

        }

        information.setUpdateUser(userId);
        information.setUpdateTime(new Date());
        informationRepository.save(information);
        String losmsg = "编辑资讯:"+information.getTitle();

        return ResultVOUtil.success(information,losmsg);
    }

    @Override
    public ResultVO deleteInformotion(Integer informationId, HttpSession session) {
        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        if (informationId == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.INFORMATION_ID_NOT_EMPTY.getCode(), languageType));
        }

        Optional<Information> informationOptional = informationRepository.findById(informationId);
        if (!informationOptional.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.INFORMATION_NOT_EXIST.getCode(), languageType));
        }
        Information information = informationOptional.get();

        informationRepository.deleteById(informationId);

        String losmsg = "删除资讯:"+information.getTitle();


        return ResultVOUtil.success(losmsg);
    }

    @Override
    public ResultVO getInformotionList(String title, Integer page, Integer size, HttpSession session) {
        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");

        if (page < 1){
            page = 1;
        }
        if (size < 1){
            size = 1;
        }

        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Information> informationPage;
        if (StringUtils.isEmpty(title)){
            informationPage = informationRepository.findAll(pageable);
        }else{
            title = "%" + title + "%";
            informationPage = informationRepository.findAllByTitleLike(title, pageable);
        }

        PageInfoDTO pageInfoDTO = new PageInfoDTO();
        pageInfoDTO.setTotalElements((int)informationPage.getTotalElements());
        pageInfoDTO.setTotalPages(informationPage.getTotalPages());
        pageInfoDTO.setPage(page);
        pageInfoDTO.setSize(size);
        pageInfoDTO.setList(informationPage.getContent());

        return ResultVOUtil.success(pageInfoDTO);

    }

}
