package com.shanzhu.book.service;

import com.shanzhu.book.model.Borrow;

import java.util.List;
import java.util.Map;

public interface BorrowService {
    Integer getCount();

    Integer getSearchCount(Map<String, Object> params);

    List<Borrow> searchBorrowsByPage(Map<String, Object> params);

    Integer addBorrow(Borrow borrow);

    Integer addBorrow2(Borrow borrow);

    Integer deleteBorrow(Borrow borrow);

    Integer deleteBorrows(List<Borrow> borrows);

    Integer updateBorrow(Borrow borrow);

    Integer updateBorrow2(Borrow borrow);

    Borrow queryBorrowsById(Integer borrowid);

    // ============ 逾期相关 ============

    Integer getOverdueCount(Map<String, Object> params);

    List<Borrow> searchOverdueByPage(Map<String, Object> params);

    List<Borrow> getCurrentOverdue();

    Map<String, Object> getOverdueStatistics();

    // ============ 续借与借书资格 ============

    /**
     * 续借图书
     * @param borrowId 借阅ID
     * @return 结果码：1=成功，0=失败，2=已达续借上限，3=已逾期不可续借
     */
    Integer renewBook(Integer borrowId);

    /**
     * 检查读者是否可以借书
     * @param userId 用户ID
     * @return 结果Map：canBorrow(可借/不可借), reason(不可借原因), overdueList(逾期未还列表), unpaidFines(未缴罚金列表)
     */
    Map<String, Object> checkUserCanBorrow(Integer userId);

    /**
     * 查询某用户对某本书的活跃借阅记录（未归还）
     */
    Borrow queryActiveBorrow(Integer userId, Integer bookId);
}