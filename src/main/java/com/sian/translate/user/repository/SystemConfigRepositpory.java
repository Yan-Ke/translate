package com.sian.translate.user.repository;

import com.sian.translate.user.entity.SystemConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SystemConfigRepositpory extends JpaRepository<SystemConfig,Integer> {



    Page<SystemConfig> findAll(Specification<SystemConfig> specification, Pageable pageable);


    void deleteAllByType(Integer type);

    List<SystemConfig> findAll(Specification<SystemConfig> specification, Sort sort);


}
