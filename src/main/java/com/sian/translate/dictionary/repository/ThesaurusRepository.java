package com.sian.translate.dictionary.repository;

import com.sian.translate.dictionary.enity.Thesaurus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThesaurusRepository extends JpaRepository<Thesaurus,Integer> {


    Thesaurus findFirstByContentOneAndType(String contentOne,int type);

    long countByContentOne(String contentOne);

    long countByContentTwo(String contentTwo);

    Page<Thesaurus> findByDictionaryId(Integer dictionrayId, Pageable pageable);

    Page<Thesaurus> findByDictionaryIdAndAndContentOneLike(Integer dictionrayId,String contentOne, Pageable pageable);

}
