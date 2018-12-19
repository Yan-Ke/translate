package com.sian.translate.banner.service.impl;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.banner.enity.Banner;
import com.sian.translate.banner.repository.BannerRepository;
import com.sian.translate.banner.service.BannerService;
import com.sian.translate.utlis.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    BannerRepository bannerRepository;

    @Override
    public ResultVO getBannerList(String languageType) {

        int type = 0;

        if (!StringUtils.isEmpty(languageType) && languageType.equals("1")){
            type = 1;
        }

        List<Banner> bannerList = bannerRepository.getAllByType(type);
        if (bannerList == null){
            bannerList = new ArrayList<>();
        }
        return ResultVOUtil.success(bannerList);

    }
}
