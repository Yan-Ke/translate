package com.sian.translate.dictionary.repository;

import com.sian.translate.dictionary.enity.Dictionary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DictionaryRepository extends JpaRepository<Dictionary,Integer> {


    List<Dictionary> findAll(Specification<Dictionary> specification);


    Page<Dictionary> findAll(Pageable pageable);

}
