package com.shanzhu.book.mapper;

import com.shanzhu.book.model.Payment;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PaymentMapper {

    int insert(Payment record);

    List<Payment> selectByUserId(@Param("userid") Integer userid);

    List<Payment> selectAll(@Param("begin") Integer begin, @Param("size") Integer size);

    int selectCount();

    int selectCountBySearch(Map<String, Object> searchParam);

    List<Payment> selectBySearch(Map<String, Object> searchParam);

    int checkPaymentExists(@Param("borrowid") Integer borrowid);

    int deleteByBorrowId(@Param("borrowid") Integer borrowid);
}