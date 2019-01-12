package com.sian.translate.DTO;

import lombok.Data;

import java.math.BigDecimal;

/***
 * 财务流水
 */
@Data
public class FinancialInfoDTO {

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    private Date payTime;

    private String payTimeString;


    /**日订单数量**/
    private long dayCount;

    /**日订单金额**/
    private BigDecimal dayAmount;

    /**订单总数量**/
    private long totalCount;

    /**订单总金额**/
    private BigDecimal totalAmount;

}
