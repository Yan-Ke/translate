package com.sian.translate.banner.repository;

import com.sian.translate.banner.enity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner,Integer> {


    List<Banner> getAllByType(Integer type);

}
