package com.sian.translate.information.repository;

import com.sian.translate.information.enity.Information;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InformationRepository extends JpaRepository<Information,Integer> {


    Page<Information> findAllByLanguageTypeAndIsShow(Integer languageType,Integer status,Pageable pageable);

    Page<Information> findAllByLanguageTypeAndIsShowAndTypeAndAdvLocationAndRecommend(Integer languageType,Integer status,Integer type,Integer advLocation,Integer recommend,Pageable pageable);


    Page<Information> findAll(Pageable pageable);

    Page<Information> findAllByTitleLike(String title,Pageable pageable);

}
