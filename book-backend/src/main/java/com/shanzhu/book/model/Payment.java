package com.shanzhu.book.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 缴费记录
 *
 * @author: ShanZhu
 * @date: 2024-01-01
 */
@Data
public class Payment {

    /**
     * 缴费ID
     */
    private Integer paymentid;

    /**
     * 借阅ID
     */
    private Integer borrowid;

    /**
     * 用户ID
     */
    private Integer userid;

    /**
     * 缴费金额
     */
    private BigDecimal amount;

    /**
     * 缴费时间
     */
    private Date paymenttime;

    /**
     * 缴费时间str
     */
    private String paymenttimestr;

    /**
     * 缴费类型（1=罚金缴费）
     */
    private Integer paymenttype;

    /**
     * 备注
     */
    private String remark;

}