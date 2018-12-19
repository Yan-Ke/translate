package com.sian.translate.member.repository;

import com.sian.translate.member.enity.MemberConfig;
import com.sian.translate.member.enity.MemberPayRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;


public interface MemberPayRecordRepository extends JpaRepository<MemberPayRecord,Integer> {


    Page<MemberPayRecord> findByUserId(String userId, Pageable pageable);


    /***
     * 根据订单id查询用户id
     * @param orderId
     * @return
     */
    MemberPayRecord findByOrderId(String orderId);

    /****
     * 根据订单id更新订单状态
     * @param orderId
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE user_order set `status` = ?1 WHERE order_id = ?2 ", nativeQuery = true)
    Integer updateByOrderId(Integer status,String orderId);

}
