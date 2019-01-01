package com.sian.translate.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/***
 * 财务流水
 */
@Data
public class FinancialInfoDTO {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    /**日订单数量**/
    private long dayCount;

    /**日订单金额**/
    private BigDecimal dayAmount;

    /**订单总数量**/
    private long totalCount;

    /**订单总金额**/
    private BigDecimal totalAmount;

}
