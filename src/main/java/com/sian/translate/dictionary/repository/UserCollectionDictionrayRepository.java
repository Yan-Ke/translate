package com.sian.translate.dictionary.repository;

import com.sian.translate.dictionary.enity.UserCollectionDictionary;
import com.sian.translate.dictionary.enity.UserTranslateRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCollectionDictionrayRepository extends JpaRepository<UserCollectionDictionary,Integer> {


    Page<UserCollectionDictionary> findByUserId(Integer userId, Pageable pageable);


}
