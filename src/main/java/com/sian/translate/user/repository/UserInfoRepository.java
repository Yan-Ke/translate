package com.sian.translate.user.repository;


import com.sian.translate.user.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface UserInfoRepository extends JpaRepository<UserInfo,Integer> {

    /***
     * 根据手机好获取用户信息
     * @param phone
     * @return
     */
    UserInfo findByPhone(String phone);


    /***通过手机查找该用户数量**/
    int countByPhone(String phone);

    /***通过qqopenid获取用户信息**/
    UserInfo findByQqOpenid(String qqOpenid);

    /***通过weixinopenid获取用户信息**/
    UserInfo findByWeixinOpenid(String weixinOpenid);

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
//    @Query(value = "select * from user_info where member_end_time >= NOW() ORDER BY ?#{#pageable}", nativeQuery = true)
    @Query(value ="select * from user_info where member_end_time >= NOW()"
            + " order by registration_time desc  limit ?1,?2 ",nativeQuery = true)
    List<UserInfo> findAllMemberUsers(Integer pageNumber, Integer pageSize);

    /***
     * 获取所有非会员信息
     */
//    @Query(value = "select * from user_info where member_end_time < NOW() or member_end_time is null ORDER BY ?#{#pageable}", nativeQuery = true)
//    @Query(value = "select * from user_info where member_end_time < NOW() or member_end_time is null ORDER BY ?#{#pageable}",
//            countQuery="select count(*) from user_info where member_end_time < NOW() or member_end_time is null",
//            nativeQuery = true)
//    Page<UserInfo> findAllUnmemberUsers(Pageable pageable);

    @Query(value ="select * from user_info where member_end_time < NOW() or member_end_time is null"
            + " order by registration_time desc  limit ?1,?2 ",nativeQuery = true)
    List<UserInfo> findAllUnmemberUsers(Integer pageNumber, Integer pageSize);


    /****
     * 获取所有用户信息
     * @return
     */
//    @Query(value = "select * from user_info ORDER BY ?#{#pageable}", nativeQuery = true)
    @Query(value ="select * from user_info "
            + " order by registration_time desc  limit ?1,?2 ",nativeQuery = true)
    List<UserInfo> findAll(Integer pageNumber, Integer pageSize);



}
