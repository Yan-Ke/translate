package com.sian.translate.management.systemset.repository;

import com.sian.translate.management.systemset.enity.Notify;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotifyRepository extends JpaRepository<Notify,Integer> {

    Page<Notify> findAll(Specification<Notify> specification, Pageable pageable);
}
