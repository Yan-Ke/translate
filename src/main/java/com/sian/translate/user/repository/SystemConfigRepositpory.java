package com.sian.translate.user.repository;

import com.sian.translate.user.entity.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemConfigRepositpory extends JpaRepository<SystemConfig,Integer> {


    SystemConfig findByType(Integer type);
}
