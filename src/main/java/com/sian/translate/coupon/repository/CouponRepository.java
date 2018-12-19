package com.sian.translate.coupon.repository;

import com.sian.translate.coupon.enity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CouponRepository extends JpaRepository<Coupon,Integer> {

    /***
     * 获取该用户所有优惠卷
     * 排序规则 1.过期最后 2.未使用的排前面 3.过期时间更近的排前面
     * @param userId
     * @return
     */
    @Query(value = "SELECT c.* FROM user_mid_coupon as umc INNER JOIN coupon as c on umc.coupon_id = c.id WHERE umc.user_id = ?1 ORDER BY c.end_time < NOW(),c.status,c.end_time ", nativeQuery = true)
    List<Coupon> findAllCoupon(Integer userId);

    /***
     * 获取该用户所有未使用优惠卷
     * 排序规则 1.过期时间更近的排前面
     * @param userId
     * @return
     */
    @Query(value = "SELECT c.* FROM user_mid_coupon as umc INNER JOIN coupon as c on umc.coupon_id = c.id WHERE umc.user_id = ?1  and c.status = 0 ORDER BY c.end_time < NOW(), c.end_time ", nativeQuery = true)
    List<Coupon> findUnusedCoupon(Integer userId);

    /***
     * 获取该用户已使用优惠卷
     * 排序规则 1.过期时间更近的排前面
     * @param userId
     * @return
     */
    @Query(value = "SELECT c.* FROM user_mid_coupon as umc INNER JOIN coupon as c on umc.coupon_id = c.id WHERE umc.user_id = ?1 and c.status = 1 ORDER BY c.end_time ", nativeQuery = true)
    List<Coupon> findUsedCoupon(Integer userId);


    /***
     * 获取该用户过期优惠卷
     * 排序规则 1.过期时间更近的排前面
     * @param userId
     * @return
     */
    @Query(value = "SELECT c.* FROM user_mid_coupon as umc INNER JOIN coupon as c on umc.coupon_id = c.id WHERE umc.user_id = ?1 and c.end_time < NOW() ORDER BY c.end_time ", nativeQuery = true)
    List<Coupon> finDoverdueCoupon(Integer userId);
}
