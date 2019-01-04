package com.sian.translate.management.user.repository;


import com.sian.translate.management.user.entity.ManageResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManageResourceRepository extends JpaRepository<ManageResource,Integer> {

    List<ManageResource> findAllByIdIn(List<Integer> ids);

}
