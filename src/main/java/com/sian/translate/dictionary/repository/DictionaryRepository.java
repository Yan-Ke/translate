package com.sian.translate.dictionary.repository;

import com.sian.translate.dictionary.enity.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DictionaryRepository extends JpaRepository<Dictionary,Integer> {


    Dictionary findByChinese(String chinese);

    Dictionary findByZang(String zang);

    Dictionary findBySanskirt(String sanskirt);

    Dictionary findByJapanese(String japanese);

    Dictionary findByEnglish(String english);


}
