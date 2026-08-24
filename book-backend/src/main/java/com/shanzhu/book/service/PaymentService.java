package com.shanzhu.book.service;

import com.shanzhu.book.model.Payment;

import java.util.List;
import java.util.Map;

public interface PaymentService {

    /**
     * 缴费（为逾期记录缴纳罚金）
     * @param borrowId 借阅ID
     * @param userId 用户ID
     * @return 结果：1=成功，0=失败，2=已缴费无需重复
     */
    Integer pay(Integer borrowId, Integer userId);

    /**
     * 查询某读者的缴费记录
     * @param userId 用户ID
     * @return 缴费记录列表
     */
    List<Payment> getPaymentsByUserId(Integer userId);

    /**
     * 分页查询所有缴费记录
     */
    List<Payment> searchPaymentsByPage(Map<String, Object> params);

    /**
     * 缴费记录总数
     */
    Integer getPaymentCount(Map<String, Object> params);

    /**
     * 查询某读者未缴费的逾期记录
     * @param userId 用户ID
     * @return 未缴费逾期借阅列表（含书名、罚金等）
     */
    List<Map<String, Object>> getUnpaidOverdueByUserId(Integer userId);
}