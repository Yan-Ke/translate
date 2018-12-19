package com.sian.translate.hint.repository;


import com.sian.translate.hint.enity.HintMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HintMessageRepository extends JpaRepository<HintMessage,Integer> {

    /****
     * 通过id查询提示信息
     * @param id
     * @return
     */
    @Query(value = "select chinese_message from hint_message where id=?1", nativeQuery = true)
    String getChineseMessage(Integer id);

    /****
     * 通过id查询藏文提示信息
     * @param id
     * @return
     */
    @Query(value = "select zang_message from hint_message where id=?1", nativeQuery = true)
    String getZangMessage(Integer id);
}