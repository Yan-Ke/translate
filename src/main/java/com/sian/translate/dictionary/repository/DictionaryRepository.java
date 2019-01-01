package com.sian.translate.dictionary.repository;

import com.sian.translate.dictionary.enity.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DictionaryRepository extends JpaRepository<Dictionary,Integer> {


}
