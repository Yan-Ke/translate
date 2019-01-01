package com.sian.translate.information.repository;

import com.sian.translate.information.enity.Information;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InformationRepository extends JpaRepository<Information,Integer> {


    Page<Information> findAllByLanguageType(Integer languageType,Pageable pageable);


    Page<Information> findAll(Pageable pageable);

    Page<Information> findAllByTitleLike(String title,Pageable pageable);

}
