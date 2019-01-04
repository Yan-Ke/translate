package com.sian.translate.management.log.repositiory;

import com.sian.translate.management.log.enity.ManageLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManageLogRepositiory extends JpaRepository<ManageLog,Integer> {

    Page<ManageLog> findAllByTypeIsNot(Integer type, Pageable pageable);
}
