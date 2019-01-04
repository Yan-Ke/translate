package com.sian.translate.user.repository;

import com.sian.translate.user.entity.UserFeedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFeedbackRepositpory extends JpaRepository<UserFeedback,Integer> {


    Page<UserFeedback> findAll(Specification<UserFeedback> spec, Pageable pageable);


    long countByStatus(Integer status);

}
