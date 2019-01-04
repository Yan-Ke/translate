package com.sian.translate.DTO;


import lombok.Data;

import java.math.BigDecimal;

/***
 * 系统首页统计数量
 */

@Data
public class StatisticalCountDTO {

    /**用户总数**/
    long totalUserCount;

    /**支付成功的订单总数**/
    long totalOrderCount;

    /**词条总条数**/
    long totalThesaurusCount;

    /**资讯总条数**/
    long totalInformationCount;


    /**今日用户新增数**/
    long todayUserCount;

    /**今日订单新增数**/
    long todayOrderCount;

    /**今日订单新增金额**/
    BigDecimal todayOrderAmount;

    /**待处理反馈条数**/
    long feeedBackCount;

    public StatisticalCountDTO(long totalUserCount, long totalOrderCount, long totalThesaurusCount, long totalInformationCount, long todayUserCount, long todayOrderCount, BigDecimal todayOrderAmount, long feeedBackCount) {
        this.totalUserCount = totalUserCount;
        this.totalOrderCount = totalOrderCount;
        this.totalThesaurusCount = totalThesaurusCount;
        this.totalInformationCount = totalInformationCount;
        this.todayUserCount = todayUserCount;
        this.todayOrderCount = todayOrderCount;
        this.todayOrderAmount = todayOrderAmount;
        this.feeedBackCount = feeedBackCount;
    }
}
