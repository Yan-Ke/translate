package com.sian.translate.dictionary.repository;

import com.sian.translate.dictionary.enity.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DictionaryRepository extends JpaRepository<Dictionary,Integer> {


    Dictionary findByChinese(String chinese);

    Dictionary findByZang(String zang);

    Dictionary findBySanskirt(String sanskirt);

    Dictionary findByJapanese(String japanese);

    Dictionary findByEnglish(String english);


    long countByChinese(String chinese);

    long countByZang(String zang);

    long countBySanskirt(String sanskirt);

    long countByJapanese(String japanese);

    long countByEnglish(String english);



}
