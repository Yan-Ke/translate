package com.sian.translate.management.systemset.repository;

import com.sian.translate.management.systemset.enity.HelpCenter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelpCenterRepository extends JpaRepository<HelpCenter,Integer> {

    Page<HelpCenter> findAll(Specification<HelpCenter> specification, Pageable pageable);

    Page<HelpCenter> findAllByStatus(Integer status,Pageable pageable);

}
