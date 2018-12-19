package com.sian.translate.user.repository;


import com.sian.translate.member.enity.MemberPayRecord;
import com.sian.translate.user.entity.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;

public interface UserInfoRepository extends JpaRepository<UserInfo,Integer> {

    /***
     * 根据手机好获取用户信息
     * @param phone
     * @return
     */
    UserInfo findByPhone(String phone);


    /***
     * 根据用户id更新会员时间
     * @param memberBeginTime
     * @param memberEndTime
     * @param id
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE user_info set member_begin_time = ?1,member_end_time = ?2 where  id = ?3", nativeQuery = true)
    void updateMemberDateById(Date memberBeginTime, Date memberEndTime,Integer id);

    /***
     * 获取所有会员信息
     */
    @Query(value = "select * from user_info where member_end_time >= NOW() ORDER BY ?#{#pageable}", nativeQuery = true)
    Page<UserInfo> findAllMemberUsers(Pageable pageable);

    /***
     * 获取所有非会员信息
     */
    @Query(value = "select * from user_info where member_end_time < NOW() or member_end_time is null ORDER BY ?#{#pageable}", nativeQuery = true)
    Page<UserInfo> findAllUnmemberUsers(Pageable pageable);

    /****
     * 获取所有用户信息
     * @param pageable
     * @return
     */
//    @Query(value = "select * from user_info ORDER BY ?#{#pageable}", nativeQuery = true)
    Page<UserInfo> findAll(Pageable pageable);



}
