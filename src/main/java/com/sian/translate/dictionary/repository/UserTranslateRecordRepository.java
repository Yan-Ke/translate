package com.sian.translate.dictionary.repository;

import com.sian.translate.dictionary.enity.UserTranslateRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTranslateRecordRepository extends JpaRepository<UserTranslateRecord,Integer> {


    Page<UserTranslateRecord> findByUserId(Integer userId, Pageable pageable);


    Page<UserTranslateRecord> findAll(Specification<UserTranslateRecord> userTranslateRecordSpecification, Pageable pageable);
}
