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

    /****
     * 获取今日支付成功的订单数量
     * @return
     */
    @Query(value = "select count(1) from user_order where to_days(pay_time) = to_days(now())  and status = 1", nativeQuery = true)
    long getTodayCount();


    /****
     * 获取今日支付成功的订单金额
     * @return
     */
    @Query(value = "select SUM(amount) from user_order where to_days(pay_time) = to_days(now())  and status = 1", nativeQuery = true)
    BigDecimal getTodayAmount();

    /***
     * 根据订单状态查询总金额
     * @param status
     * @return
     */
    @Query(value = "SELECT SUM(amount) FROM user_order WHERE status = ?1 ", nativeQuery = true)
    BigDecimal getSumAmountByStatus(Integer status);



    /****
     * 获取7日内数据
     * @return
     */
    @Query(value = "select a.click_date,ifnull(b.count,0) as count\n" +
            "from (\n" +
            "    SELECT curdate() as click_date\n" +
            "    union all\n" +
            "    SELECT date_sub(curdate(), interval 1 day) as click_date\n" +
            "    union all\n" +
            "    SELECT date_sub(curdate(), interval 2 day) as click_date\n" +
            "    union all\n" +
            "    SELECT date_sub(curdate(), interval 3 day) as click_date\n" +
            "    union all\n" +
            "    SELECT date_sub(curdate(), interval 4 day) as click_date\n" +
            "    union all\n" +
            "    SELECT date_sub(curdate(), interval 5 day) as click_date\n" +
            "    union all\n" +
            "    SELECT date_sub(curdate(), interval 6 day) as click_date\n" +
            ") a left join (\n" +
            "  select date(pay_time) as pay_time, count(*) as count\n" +
            "  from user_order\n" +
            " group by date(pay_time)" +
            ") b on a.click_date = b.pay_time order by a.click_date ;", nativeQuery = true)
    List<Object[]> getSevenDayOrderCount();


    /****
     * 获取30天订单数量
     * @return
     */
    @Query(value = "select a.click_date,ifnull(b.count,0) as count\n" +
            "from (\n" +
            "    SELECT curdate() as click_date\n" +
            "    union all\n" +
            "    SELECT date_sub(curdate(), interval 1 day) as click_date\n" +
            "    union all\n" +
            "    SELECT date_sub(curdate(), interval 2 day) as click_date\n" +
            "    union all\n" +
            "    SELECT date_sub(curdate(), interval 3 day) as click_date\n" +
            "    union all\n" +
            "    SELECT date_sub(curdate(), interval 4 day) as click_date\n" +
            "    union all\n" +
            "    SELECT date_sub(curdate(), interval 5 day) as click_date\n" +
            "    union all\n" +
            "    SELECT date_sub(curdate(), interval 6 day) as click_date\n" +
            "  union all\n" +
            "    SELECT date_sub(curdate(), interval 7 day) as click_date\n" +
            "  union all\n" +
            "    SELECT date_sub(curdate(), interval 8 day) as click_date\n" +
            "  union all\n" +
            "    SELECT date_sub(curdate(), interval 9 day) as click_date\n" +
            "  union all\n" +
            "    SELECT date_sub(curdate(), interval 10 day) as click_date\n" +
            "  union all\n" +
            "    SELECT date_sub(curdate(), interval 11 day) as click_date\n" +
            "  union all\n" +
            "    SELECT date_sub(curdate(), interval 12 day) as click_date\n" +
            "  union all\n" +
            "    SELECT date_sub(curdate(), interval 13 day) as click_date\n" +
            "  union all\n" +
            "    SELECT date_sub(curdate(), interval 14 day) as click_date\n" +
            "  union all\n" +
            "    SELECT date_sub(curdate(), interval 15 day) as click_date\n" +
            " union all\n" +
            "    SELECT date_sub(curdate(), interval 16 day) as click_date\n" +
            "    union all\n" +
            "    SELECT date_sub(curdate(), interval 17 day) as click_date\n" +
            "    union all\n" +
            "    SELECT date_sub(curdate(), interval 18 day) as click_date\n" +
            "    union all\n" +
            "    SELECT date_sub(curdate(), interval 19 day) as click_date\n" +
            "    union all\n" +
            "    SELECT date_sub(curdate(), interval 20 day) as click_date\n" +
            "    union all\n" +
            "    SELECT date_sub(curdate(), interval 21 day) as click_date\n" +
            "  union all\n" +
            "    SELECT date_sub(curdate(), interval 22 day) as click_date\n" +
            "  union all\n" +
            "    SELECT date_sub(curdate(), interval 23 day) as click_date\n" +
            "  union all\n" +
            "    SELECT date_sub(curdate(), interval 24 day) as click_date\n" +
            "  union all\n" +
            "    SELECT date_sub(curdate(), interval 25 day) as click_date\n" +
            "  union all\n" +
            "    SELECT date_sub(curdate(), interval 26 day) as click_date\n" +
            "  union all\n" +
            "    SELECT date_sub(curdate(), interval 27 day) as click_date\n" +
            "  union all\n" +
            "    SELECT date_sub(curdate(), interval 28 day) as click_date\n" +
            "  union all\n" +
            "    SELECT date_sub(curdate(), interval 29 day) as click_date\n" +
            " \n" +
            ") a left join (\n" +
            "  select date(pay_time) as pay_time, count(*) as count\n" +
            "  from user_order\n" +
            "  group by date(pay_time)\n" +
            ") b on a.click_date = b.pay_time order by a.click_date;", nativeQuery = true)
    List<Object[]> getMonthOrderCount();



}
