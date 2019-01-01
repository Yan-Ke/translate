package com.sian.translate.coupon.repository;

import com.sian.translate.coupon.enity.UserCouponRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;

public interface UserCouponRecordRepository extends JpaRepository<UserCouponRecord,Integer> {

    Page<UserCouponRecord> findByCouponNameLike(Pageable pageable,String couponName);

    Page<UserCouponRecord> findAll(Pageable pageable);


    @Modifying
    @Transactional
    @Query(value = "update user_coupon_record set use_time = ?1,use_order_no = ?2 WHERE user_mid_coupon_id = ?3", nativeQuery = true)
    void updateByuserMidCouponId(Date useTime, String useOrderNo, Integer userMidCouponId);
}
