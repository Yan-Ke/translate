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
    @Query(value = "UPDATE user_info set member_begin_time = ?1,member_end_time = ?2,member_month = ?3,buy_time = ?4 where  id = ?5", nativeQuery = true)
    void updateMemberDateById(Date memberBeginTime, Date memberEndTime,Integer memberMonth,Date buyTime,Integer id);

    /***
     * 获取所有会员信息
     */
//    @Query(value = "select * from user_info where member_end_time >= NOW() ORDER BY ?#{#pageable}", nativeQuery = true)
    @Query(value ="select * from user_info where member_end_time >= NOW()"
            + " order by registration_time desc  limit ?1,?2 ",nativeQuery = true)
    List<UserInfo> findAllMemberUsers(Integer pageNumber, Integer pageSize);
    @Query(value ="select count(1) from user_info "
            + " where member_end_time >= NOW()",nativeQuery = true)
    long  countAllMembers();

    @Query(value ="select * from user_info where member_end_time >= NOW()"
            + " and (phone like ?3 or nick_name like ?3) order by registration_time desc  limit ?1,?2 ",nativeQuery = true)
    List<UserInfo> findAllMemberUsersByPhoneOrNickName(Integer pageNumber, Integer pageSize,String param);
    @Query(value ="select count(1) from user_info "
            + " where member_end_time >= NOW() and (phone like ?1 or nick_name like ?1) ",nativeQuery = true)
    long  countAllMembersByPhoneOrNickName(String param);

    /***
     * 获取所有非会员信息(不包括过期会员)
     */
//    @Query(value = "select * from user_info where member_end_time < NOW() or member_end_time is null ORDER BY ?#{#pageable}", nativeQuery = true)
//    @Query(value = "select * from user_info where member_end_time < NOW() or member_end_time is null ORDER BY ?#{#pageable}",
//            countQuery="select count(*) from user_info where member_end_time < NOW() or member_end_time is null",
//            nativeQuery = true)
//    Page<UserInfo> findAllUnmemberUsers(Pageable pageable);

    @Query(value ="select * from user_info where member_end_time is null"
            + " order by registration_time desc  limit ?1,?2 ",nativeQuery = true)
    List<UserInfo> findAllUnmemberUsers(Integer pageNumber, Integer pageSize);
    @Query(value ="select count(1) from user_info "
            + " where member_end_time is null ",nativeQuery = true)
    long  countAllUnmembers();

    @Query(value ="select * from user_info where member_end_time is null"
            + " and (phone like ?3 or nick_name like ?3) order by registration_time desc  limit ?1,?2 ",nativeQuery = true)
    List<UserInfo> findAllUnmemberUsersByPhoneOrNickName(Integer pageNumber, Integer pageSize,String param);
    @Query(value ="select count(1) from user_info "
            + " where member_end_time is null and (phone like ?1 or nick_name like ?1) ",nativeQuery = true)
    long  countAllUnmemberByPhoneOrNickName(String param);

    /***4
     * 获取所有过期会员
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Query(value ="select * from user_info where member_end_time < NOW() and member_end_time is not null"
            + " order by registration_time desc  limit ?1,?2 ",nativeQuery = true)
    List<UserInfo> findOverdueUnmemberUsers(Integer pageNumber, Integer pageSize);

    @Query(value ="select count(1) from user_info "
            + " where  member_end_time < NOW() and member_end_time is not null ",nativeQuery = true)
    long  countOverdueMembers();

    @Query(value ="select * from user_info where member_end_time < NOW() and member_end_time is not null"
            + " and (phone like ?3 or nick_name like ?3) order by registration_time desc  limit ?1,?2 ",nativeQuery = true)
    List<UserInfo> findOverdueUnmemberUsersByPhoneOrNickName(Integer pageNumber, Integer pageSize,String param);

    @Query(value ="select count(1) from user_info "
            + " where  member_end_time < NOW() and member_end_time is not null and (phone like ?1 or nick_name like ?1) ",nativeQuery = true)
    long countOverdueMembersByPhoneOrNickName(String param);

    /****
     * 获取所有用户信息
     * @return
     */
//    @Query(value = "select * from user_info ORDER BY ?#{#pageable}", nativeQuery = true)
    @Query(value ="select * from user_info "
            + " order by registration_time desc  limit ?1,?2 ",nativeQuery = true)
    List<UserInfo> findAll(Integer pageNumber, Integer pageSize);


    @Query(value ="select * from user_info "
            + " where phone like ?3 or nick_name like ?3 order by registration_time desc  limit ?1,?2 ",nativeQuery = true)
    List<UserInfo> findAllByPhoneOrNickName(Integer pageNumber, Integer pageSize,String param);

    @Query(value ="select count(1) from user_info "
            + " where phone like ?1 or nick_name like ?1 ",nativeQuery = true)
    long  countAllByPhoneOrNickName(String param);


    List<UserInfo> findByIdIn(List<Integer> users);


    List<UserInfo> findByMemberEndTimeIsNotNull();

    List<UserInfo> findByMemberEndTimeIsNull();


    /**获取今日用户新增数量**/
    @Query(value = "select count(1) from user_info where to_days(registration_time) = to_days(now())", nativeQuery = true)
    long getTodayCount();

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
            "  select date(registration_time) as registration_time, count(*) as count\n" +
            "  from user_info\n" +
            "  group by date(registration_time)\n" +
            ") b on a.click_date = b.registration_time;\n", nativeQuery = true)
    List<Object[]> getSevenDayUserCount();

    /****
     * 获取30天用户新增数量
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
            "  select date(registration_time) as registration_time, count(*) as count\n" +
            "  from user_info\n" +
            "  group by date(registration_time)\n" +
            ") b on a.click_date = b.registration_time;", nativeQuery = true)
    List<Object[]> getMonthUserCount();



}
