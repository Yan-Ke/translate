package com.sian.translate.information.repository;

import com.sian.translate.information.enity.UserMidInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMidInformationRepoitory extends JpaRepository<UserMidInformation,Integer> {

    UserMidInformation findByUserIdAndInfomationIdAndType(Integer userId,Integer informationID,Integer type);
}
