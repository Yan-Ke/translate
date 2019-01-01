package com.sian.translate.member.repository;

import com.sian.translate.member.enity.MemberPayRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;


public interface MemberPayRecordRepository extends JpaRepository<MemberPayRecord,Integer> {


    /***
     * 获取某一用户购买记录
     * @param userId
     * @param pageable
     * @return
     */
    Page<MemberPayRecord> findByUserId(Integer userId, Pageable pageable);

    /***
     * 获取所有用户购买记录
     * @return
     */
    @Query(value ="SELECT uo.id as id,uo.user_id as userId,uo.amount as amount,uo.month as month,uo.pay_time as payTime,uo.coupon_id as counpnId,uo.reduce_amount as reduceAmount,uo.status as status,uo.order_id as orderId,uo.pay_type as payType,ui.nick_name as nickName,ui.head_big_image as headBigImage,ui.member_begin_time as memberBeginTime,ui.member_end_time as memberEndTime,c.name as couponName from user_order as uo LEFT JOIN user_info as ui on ui.id = uo.user_id left join coupon as c on c.id = uo.coupon_id order by uo.pay_time desc  limit ?1,?2 ",nativeQuery = true)
//    @Query(value ="SELECT uo.id as id,uo.user_id as userId,uo.amount as amount,uo.month as month,uo.pay_time as payTime,uo.coupon_id as counpnId,uo.reduce_amount as reduceAmount,uo.status as status,uo.order_id as orderId,uo.pay_type as payType,ui.nick_name as nickName from user_order as uo LEFT JOIN user_info as ui on ui.id = uo.user_id  order by uo.pay_time desc  limit ?1,?2 ",nativeQuery = true)
    List<Object[]> findAllMemberRecords(Integer pageNumber, Integer pageSize);

//    @Query(value ="select * from user_order as uo LEFT JOIN user_info as ui on ui.id = uo.user_id  order by uo.pay_time desc limit ?1,?2 ",nativeQuery = true)
//    List<MemberPayRecord> findAllMemberRecords(Integer pageNumber, Integer pageSize);

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

    /****
     * 根据订单状态查询订单数量
     * @param status
     * @return
     */
    long countByStatus(Integer status);


    /***
     * 根据订单状态查询总金额
     * @param status
     * @return
     */
    @Query(value = "SELECT SUM(amount) FROM user_order WHERE status = ?1 ", nativeQuery = true)
    BigDecimal getSumAmountByStatus(Integer status);



}
