package com.sian.translate.management.user.repository;


import com.sian.translate.management.user.entity.ManageUserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManageUserInfoRepository extends JpaRepository<ManageUserInfo,Integer> {



    ManageUserInfo findByAccount(String account);

    long countById(Integer id);

    Page<ManageUserInfo> findAllByAccountNot(String account,Pageable pageable);

    long countByAccount(String account);

}
