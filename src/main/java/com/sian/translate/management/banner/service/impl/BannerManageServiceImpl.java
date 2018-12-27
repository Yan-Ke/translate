package com.sian.translate.management.banner.service.impl;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.banner.enity.Banner;
import com.sian.translate.banner.repository.BannerRepository;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.management.banner.service.BannerManageService;
import com.sian.translate.management.user.service.ManageUserService;
import com.sian.translate.utlis.ImageUtlis;
import com.sian.translate.utlis.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Service
public class BannerManageServiceImpl implements BannerManageService {

    @Autowired
    HintMessageService hintMessageService;

    @Autowired
    BannerRepository bannerRepository;

    @Override
    public ResultVO addBanner(String content, String url,Integer type,MultipartFile file,HttpSession session) {

        String languageType = "0";


        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        if (StringUtils.isEmpty(content)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.BANNER_CONTENT_NOT_EMPTY.getCode(), languageType));
        }
        if (StringUtils.isEmpty(url)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.BANNER_URL_NOT_EMPTY.getCode(), languageType));
        }

        if (type == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.BANNER_LANGUAGE_NOT_EMPTY.getCode(), languageType));
        }
        if (file == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.IMG_NOT_EMPTY.getCode(), languageType));
        }
        String imagePath = "";
        try {
            imagePath = ImageUtlis.loadImage(file);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.IMG_FORMAT_ERROR.getCode(), languageType));
        }

        Banner banner = new Banner();
        banner.setContent(content);
        if (!url.startsWith("http://")){
            url = "http://"+url;
        }
        banner.setUrl(url);
        banner.setImage(imagePath);
        banner.setType(type);
        banner.setCreateUser(userId);
        banner.setCreateTime(new Date());
        banner.setUpdateUser(userId);
        banner.setUpdateTime(new Date());

        bannerRepository.save(banner);

        return ResultVOUtil.success(banner);
    }

    @Override
    public ResultVO editBanner(Integer bannerId,String content, String url, Integer type, MultipartFile file, HttpSession session) {
        String languageType = "0";


        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }
        if (bannerId == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.BANNER_ID_NOT_EMPTY.getCode(), languageType));
        }

        /**
         * 输入了内容必须传入语言类型
         */
        if (!StringUtils.isEmpty(content) && type == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.BANNER_LANGUAGE_NOT_EMPTY.getCode(), languageType));
        }

        String imagePath = "";

        if (file != null){
            try {
                imagePath = ImageUtlis.loadImage(file);
            } catch (IOException e) {
                e.printStackTrace();
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.IMG_FORMAT_ERROR.getCode(), languageType));
            }
        }

        Optional<Banner> bannerOptional = bannerRepository.findById(bannerId);

        if (!bannerOptional.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.BANNER_NOT_EXIST.getCode(), languageType));
        }


        Banner banner = bannerOptional.get();
        if (!StringUtils.isEmpty(content)){
            banner.setContent(content);
        }
        if (!StringUtils.isEmpty(url)){
            if (!url.startsWith("http://")){
                url = "http://"+url;
            }
            banner.setUrl(url);
        }
        if (!StringUtils.isEmpty(imagePath)){
            banner.setImage(imagePath);
        }
        if (type != null){
            banner.setType(type);
        }
        banner.setUpdateUser(userId);
        banner.setUpdateTime(new Date());
        bannerRepository.save(banner);

        return ResultVOUtil.success(banner);    }

    @Override
    public ResultVO deleteBanner(Integer bannerId, HttpSession session) {
        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        if (bannerId == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.BANNER_ID_NOT_EMPTY.getCode(), languageType));
        }

        Optional<Banner> informationOptional = bannerRepository.findById(bannerId);
        if (!informationOptional.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.BANNER_NOT_EXIST.getCode(), languageType));
        }
        bannerRepository.deleteById(bannerId);


        return ResultVOUtil.success();
    }
}
