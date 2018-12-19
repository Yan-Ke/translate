package com.sian.translate.banner.service;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.banner.enity.Banner;

import java.util.List;

public interface BannerService {

    ResultVO getBannerList(String languageType);

}
