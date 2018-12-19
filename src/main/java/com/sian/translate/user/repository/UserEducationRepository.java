package com.sian.translate.user.repository;


import com.sian.translate.user.entity.UserEducation;
import com.sian.translate.user.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserEducationRepository extends JpaRepository<UserEducation,Integer> {

    @Query(value = "select education_name from user_education", nativeQuery = true)
    List<String> getAllChineseEducationName();

    @Query(value = "select zang_education_name from user_education", nativeQuery = true)
    List<String> getAllZangEducationName();

}
