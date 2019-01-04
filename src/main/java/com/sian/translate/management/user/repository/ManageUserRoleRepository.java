package com.sian.translate.management.user.repository;


import com.sian.translate.management.user.entity.ManageUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManageUserRoleRepository extends JpaRepository<ManageUserRole,Integer> {


    long countByRoleName(String roleName);

    long countById(Integer id);


}
