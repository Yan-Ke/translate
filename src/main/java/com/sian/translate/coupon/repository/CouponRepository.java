package com.sian.translate.coupon.repository;

import com.sian.translate.coupon.enity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;


public interface CouponRepository extends JpaRepository<Coupon,Integer> {

    /***
     * 获取该用户所有优惠卷
     * 排序规则 1.过期最后 2.未使用的排前面 3.过期时间更近的排前面
     * @param userId
     * @return
     */
    @Query(value = "SELECT umc.id as id,c.name as name,umc.begin_time as beginTime,umc.end_time as endTime,c.full_price as fullPrice,c.reduce_price as reducePrice,c.id as couponId FROM user_mid_coupon as umc INNER JOIN coupon as c on umc.coupon_id = c.id WHERE umc.user_id = ?1 ORDER BY umc.end_time < NOW(),umc.is_use,umc.end_time ", nativeQuery = true)
    List<Object[]> findAllCoupon(Integer userId);

    /***
     * 获取该用户所有未使用优惠卷
     * 排序规则 1.过期时间更近的排前面
     * @param userId
     * @return
     */
    @Query(value = "SELECT umc.id as id,c.name as name,umc.begin_time as beginTime,umc.end_time as endTime,c.full_price as fullPrice,c.reduce_price as reducePrice,c.id as couponId FROM user_mid_coupon as umc INNER JOIN coupon as c on umc.coupon_id = c.id WHERE umc.is_use =0 and umc.user_id = ?1  and umc.end_time > NOW() ORDER BY umc.end_time < NOW(), umc.end_time ", nativeQuery = true)
    List<Object[]> findUnusedCoupon(Integer userId);

    /***
     * 获取该用户已使用优惠卷
     * 排序规则 1.过期时间更近的排前面
     * @param userId
     * @return
     */
    @Query(value = "SELECT umc.id as id,c.name as name,umc.begin_time as beginTime,umc.end_time as endTime,c.full_price as fullPrice,c.reduce_price as reducePrice,c.id as couponId FROM user_mid_coupon as umc INNER JOIN coupon as c on umc.coupon_id = c.id WHERE umc.is_use =1 and umc.user_id = ?1  ORDER BY umc.end_time ", nativeQuery = true)
    List<Object[]> findUsedCoupon(Integer userId);


    /***
     * 获取该用户过期优惠卷
     * 排序规则 1.过期时间更近的排前面
     * @param userId
     * @return
     */
    @Query(value = "SELECT umc.id as id,c.name as name,umc.begin_time as beginTime,umc.end_time as endTime,c.full_price as fullPrice,c.reduce_price as reducePrice,c.id as couponId FROM user_mid_coupon as umc INNER JOIN coupon as c on umc.coupon_id = c.id WHERE umc.user_id = ?1 and umc.end_time < NOW()  ORDER BY umc.end_time ", nativeQuery = true)
    List<Object[]> finDoverdueCoupon(Integer userId);


    /***
     * 获取该用户待领取的所有优惠卷
     * @return
     */
    @Query(value = "SELECT * FROM coupon as c WHERE  (c.end_time > NOW() and c.count > 0) or c.type = 1 ORDER BY c.end_time < NOW(),c.end_time ", nativeQuery = true)
    List<Coupon> findAllWaitReceivceCoupon();



    /***
     * 支付时获取可以使用的优惠券列表
     * @return
     */
    @Query(value ="SELECT umc.id as id,c.name as name,umc.begin_time as beginTime,umc.end_time as endTime,c.full_price as fullPrice,c.reduce_price as reducePrice,c.id as couponId FROM user_mid_coupon AS umc INNER JOIN coupon AS c ON umc.coupon_id = c.id WHERE umc.is_use = 0 AND umc.user_id = ?1  AND c.full_price <= ?2 AND umc.end_time > NOW() AND umc.begin_time < NOW() ORDER BY umc.end_time < NOW(), umc.end_time",nativeQuery = true)
    List<Object[]> findPayCouponList(Integer userId, BigDecimal amount);




    /***
     * 通过优惠券名称查询优惠券
     * @param pageable
     * @return
     */
    Page<Coupon> findAllByNameLike(String name,Pageable pageable);

    /***
     * 查询所有优惠券
     * @param pageable
     * @return
     */
    Page<Coupon> findAll(Pageable pageable);

    /**
     * 通过id查询优惠券数量
     * @param id
     * @return
     */
    int countById(Integer id);

}
