package com.sian.translate.management.user.repository;


import com.sian.translate.management.user.entity.ManageUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManageUserInfoRepository extends JpaRepository<ManageUserInfo,Integer> {



    ManageUserInfo findByAccount(String account);


}
