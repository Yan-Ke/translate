package com.sian.translate.member.repository;

import com.sian.translate.member.enity.MemberConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MemberConfigRepository extends JpaRepository<MemberConfig,Integer> {

    @Query(value = "select * from member_config order by month desc ", nativeQuery = true)
    List<MemberConfig> getMemberConfig();

}
