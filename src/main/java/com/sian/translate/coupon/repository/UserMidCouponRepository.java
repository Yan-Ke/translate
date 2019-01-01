package com.sian.translate.coupon.repository;

import com.sian.translate.coupon.enity.UserMidCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMidCouponRepository extends JpaRepository<UserMidCoupon,Integer> {


    int countByUserIdAndCouponId(Integer userId,Integer couponId);

    void deleteByCouponId(Integer couponId);
}
