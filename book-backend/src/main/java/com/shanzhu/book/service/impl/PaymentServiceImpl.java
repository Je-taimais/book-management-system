package com.shanzhu.book.service.impl;

import com.shanzhu.book.mapper.BorrowMapper;
import com.shanzhu.book.mapper.PaymentMapper;
import com.shanzhu.book.model.Borrow;
import com.shanzhu.book.model.Payment;
import com.shanzhu.book.service.PaymentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Resource
    private PaymentMapper paymentMapper;

    @Resource
    private BorrowMapper borrowMapper;

    @Override
    public Integer pay(Integer borrowId, Integer userId) {
        // 检查是否已缴过费
        int exists = paymentMapper.checkPaymentExists(borrowId);
        if (exists > 0) {
            return 2; // 已缴费
        }

        // 查询该借阅记录
        Borrow borrow = borrowMapper.selectByPrimaryKey(borrowId);
        if (borrow == null) {
            return 0; // 记录不存在
        }

        // 获取罚金：如果记录中 fine 为 0 但已逾期未还，则实时计算
        BigDecimal fine = borrow.getFine();
        if (fine == null || fine.compareTo(BigDecimal.ZERO) <= 0) {
            // 罚金尚未计算，判断是否逾期未还
            if (borrow.getReturntime() == null && borrow.getDuedate() != null) {
                Date dueDate = borrow.getDuedate();
                Date now = new Date();
                if (dueDate.before(now)) {
                    long diffMillis = now.getTime() - dueDate.getTime();
                    int days = (int) (diffMillis / (1000 * 60 * 60 * 24));
                    if (days > 0) {
                        fine = new BigDecimal(days * 0.5).setScale(2, java.math.RoundingMode.HALF_UP);
                    }
                }
            }
        }

        if (fine == null || fine.compareTo(BigDecimal.ZERO) <= 0) {
            return 0; // 无罚金
        }

        // 创建缴费记录
        Payment payment = new Payment();
        payment.setBorrowid(borrowId);
        payment.setUserid(userId);
        payment.setAmount(fine);
        payment.setPaymenttime(new Date());
        payment.setPaymenttype(1);
        payment.setRemark("逾期罚金缴费");

        int res = paymentMapper.insert(payment);
        return res > 0 ? 1 : 0;
    }

    @Override
    public List<Payment> getPaymentsByUserId(Integer userId) {
        List<Payment> payments = paymentMapper.selectByUserId(userId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Payment p : payments) {
            if (p.getPaymenttime() != null) {
                p.setPaymenttimestr(sdf.format(p.getPaymenttime()));
            }
        }
        return payments;
    }

    @Override
    public List<Payment> searchPaymentsByPage(Map<String, Object> params) {
        List<Payment> payments = paymentMapper.selectBySearch(params);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Payment p : payments) {
            if (p.getPaymenttime() != null) {
                p.setPaymenttimestr(sdf.format(p.getPaymenttime()));
            }
        }
        return payments;
    }

    @Override
    public Integer getPaymentCount(Map<String, Object> params) {
        return paymentMapper.selectCountBySearch(params);
    }

    @Override
    public List<Map<String, Object>> getUnpaidOverdueByUserId(Integer userId) {
        List<Borrow> unpaidList = borrowMapper.selectUserUnpaidOverdue(userId);
        List<Map<String, Object>> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (Borrow b : unpaidList) {
            Map<String, Object> item = new HashMap<>();
            item.put("borrowId", b.getBorrowid());
            item.put("bookName", b.getBookname());
            item.put("borrowTime", b.getBorrowtime() != null ? sdf.format(b.getBorrowtime()) : "");
            item.put("returnTime", b.getReturntime() != null ? sdf.format(b.getReturntime()) : "");
            item.put("dueDate", b.getDuedate() != null ? sdf.format(b.getDuedate()) : "");
            item.put("overdueDays", b.getOverduedays());
            item.put("fine", b.getFine());
            result.add(item);
        }
        return result;
    }
}