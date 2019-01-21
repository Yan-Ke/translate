package com.sian.translate.dictionary.repository;

import com.sian.translate.dictionary.enity.Thesaurus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThesaurusRepository extends JpaRepository<Thesaurus,Integer> {


    Thesaurus findFirstByContentOneAndDictionaryId(String contentOne,int dictionaryId);

    long countByContentOne(String contentOne);

    long countByContentTwo(String contentTwo);

    long countByDictionaryId(Integer dictionaryId);

    Page<Thesaurus> findByDictionaryId(Integer dictionrayId, Pageable pageable);

    List<Thesaurus> findByDictionaryId(Integer dictionrayId);

//    Page<Thesaurus> findByDictionaryIdAndContentOneLikeOrContentTwoLike(Integer dictionrayId,String contentOne, Pageable pageable);

    Page<Thesaurus> findAll(Specification<Thesaurus> title, Pageable pageable);
}
