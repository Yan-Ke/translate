package com.sian.translate.user.repository;

import com.sian.translate.user.entity.SystemConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemConfigRepositpory extends JpaRepository<SystemConfig,Integer> {


    SystemConfig findByType(Integer type);

    Page<SystemConfig> findAll(Specification<SystemConfig> specification, Pageable pageable);

    void deleteByType(Integer type);
}
