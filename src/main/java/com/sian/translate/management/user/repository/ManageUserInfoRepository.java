package com.sian.translate.management.user.repository;


import com.sian.translate.management.user.entity.ManageUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;

public interface ManageUserInfoRepository extends JpaRepository<ManageUserInfo,Integer> {



    ManageUserInfo findByAccount(String account);

}
