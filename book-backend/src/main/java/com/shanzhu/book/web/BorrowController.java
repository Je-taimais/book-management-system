package com.shanzhu.book.web;

import com.shanzhu.book.exception.BookNotEnoughException;
import com.shanzhu.book.exception.OperationFailureException;
import com.shanzhu.book.model.BookInfo;
import com.shanzhu.book.model.Borrow;
import com.shanzhu.book.service.BookInfoService;
import com.shanzhu.book.service.BorrowService;
import com.shanzhu.book.service.PaymentService;
import com.shanzhu.book.utils.PageUtils;
import com.shanzhu.book.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 图书借阅 控制层
 *
 * @author: ShanZhu
 * @date: 2023-12-31
 */
@Slf4j
@RestController
@RequestMapping(value = "/borrow")
public class BorrowController {

    @Resource
    private BorrowService borrowService;

    @Resource
    private BookInfoService bookInfoService;

    @Resource
    private PaymentService paymentService;

    /**
     * 分页查询借阅
     *
     * @param params {page, limit, userid, bookid}
     * @return 借阅数据
     */
    @RequestMapping(value = "/queryBorrowsByPage")
    public Map<String, Object> queryBorrowsByPage(@RequestParam Map<String, Object> params) {
        PageUtils.parsePageParams(params);
        int count = borrowService.getSearchCount(params);
        List<Borrow> borrows = borrowService.searchBorrowsByPage(params);
        return R.getListResultMap(0, "success", count, borrows);
    }

    /**
     * 获得数量
     *
     * @return 数量
     */
    @RequestMapping(value = "/getCount")
    public Integer getCount() {
        return borrowService.getCount();
    }

    /**
     * 添加借阅
     *
     * @param borrow 借阅信息
     * @return 结果
     */
    @RequestMapping(value = "/addBorrow")
    public Integer addBorrow(@RequestBody Borrow borrow) {
        return borrowService.addBorrow(borrow);
    }

    /**
     * 删除借阅
     *
     * @param borrow 借阅信息
     * @return 结果
     */
    @RequestMapping(value = "/deleteBorrow")
    public Integer deleteBorrow(@RequestBody Borrow borrow) {
        return borrowService.deleteBorrow(borrow);
    }

    /**
     * 批量删除借阅
     *
     * @param borrows 借阅信息
     * @return 结果
     */
    @RequestMapping(value = "/deleteBorrows")
    public Integer deleteBorrows(@RequestBody List<Borrow> borrows) {
        return borrowService.deleteBorrows(borrows);
    }

    /**
     * 更新借阅
     *
     * @param borrow 借阅信息
     * @return 结果
     */
    @RequestMapping(value = "/updateBorrow")
    public Integer updateBorrow(@RequestBody Borrow borrow) {
        return borrowService.updateBorrow(borrow);
    }

    /**
     * 借书
     *
     * @param userid 用户id
     * @param bookid 书籍id
     * @return 结果
     */
    @RequestMapping(value = {"/borrowBook", "/reader/borrowBook"})
    @Transactional
    public Map<String, Object> borrowBook(Integer userid, Integer bookid) {
        try {
            // 检查读者借书资格
            Map<String, Object> canBorrowResult = borrowService.checkUserCanBorrow(userid);
            boolean canBorrow = (boolean) canBorrowResult.get("canBorrow");
            if (!canBorrow) {
                return R.getResultMap(1, (String) canBorrowResult.get("reason"), canBorrowResult);
            }

            // 查询该书的情况
            BookInfo theBook = bookInfoService.queryBookInfoById(bookid);

            if (theBook == null) {  // 图书不存在
                throw new NullPointerException("图书" + bookid + "不存在");
            } else if (theBook.getStock() == null || theBook.getStock() <= 0) {  // 库存不足
                throw new BookNotEnoughException("图书" + bookid + "库存不足（剩余0本）");
            }

            // 检查该用户是否已借阅此书且未归还（避免重复借阅）
            Borrow existingBorrow = borrowService.queryActiveBorrow(userid, bookid);
            if (existingBorrow != null) {
                throw new BookNotEnoughException("您已借阅此书，请先归还后再借");
            }

            // 更新图书库存（stock - 1），isBorrowed 由查询时动态计算
            BookInfo bookInfo = new BookInfo();
            bookInfo.setBookid(bookid);
            bookInfo.setStock(theBook.getStock() - 1);
            Integer res2 = bookInfoService.updateBookInfo(bookInfo);
            if (res2 == 0) throw new OperationFailureException("图书" + bookid + "更新被借信息失败");

            // 计算应还日期：borrowTime + 30天
            Date now = new Date(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.DAY_OF_MONTH, 30);
            Date dueDate = calendar.getTime();

            // 添加一条记录到borrow表
            Borrow borrow = new Borrow();
            borrow.setUserid(userid);
            borrow.setBookid(bookid);
            borrow.setBorrowtime(now);
            borrow.setDuedate(dueDate);
            borrow.setIsoverdue(0);
            borrow.setOverduedays(0);
            borrow.setFine(new BigDecimal("0.00"));
            borrow.setRenewcount(0);
            Integer res1 = borrowService.addBorrow2(borrow);
            if (res1 == 0) throw new OperationFailureException("图书" + bookid + "添加借阅记录失败");

        } catch (Exception e) {
            log.error("发生异常，进行手动回滚", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.getResultMap(1, "借书失败：" + e.getMessage());
        }
        return R.getResultMap(0, "借书成功");
    }

    /**
     * 还书
     *
     * @param borrowid 借阅id
     * @param bookid   书籍id
     * @return 结果Map（status: 0=成功, 1=失败; message: 提示信息; data: 逾期详情）
     */
    @RequestMapping(value = {"/returnBook", "/reader/returnBook"})
    @Transactional
    public Map<String, Object> returnBook(Integer borrowid, Integer bookid) {
        Map<String, Object> result = new java.util.HashMap<>();
        try {
            // 查询该书的情况
            BookInfo theBook = bookInfoService.queryBookInfoById(bookid);
            // 查询借书的情况
            Borrow theBorrow = borrowService.queryBorrowsById(borrowid);

            // 图书不存在
            if (theBook == null) {
                throw new NullPointerException("图书" + bookid + "不存在");
            } else if (theBorrow == null) {
                //结束记录不存在
                throw new NullPointerException("借书记录" + borrowid + "不存在");
            } else if (theBorrow.getReturntime() != null) {
                // 已经还过书
                throw new BookNotEnoughException("图书" + bookid + "已经还过了");
            }

            // 更新图书库存（stock + 1），isBorrowed 由查询时动态计算
            BookInfo bookInfo = new BookInfo();
            bookInfo.setBookid(bookid);
            int newStock = (theBook.getStock() == null ? 1 : theBook.getStock() + 1);
            bookInfo.setStock(newStock);
            Integer res2 = bookInfoService.updateBookInfo(bookInfo);
            if (res2 == 0) throw new OperationFailureException("图书" + bookid + "更新被借信息失败");

            // 计算逾期信息
            Date returnTime = new Date(System.currentTimeMillis());
            Date dueDate = theBorrow.getDuedate();

            int overdueDays = 0;
            int isOverdue = 0;
            BigDecimal fine = new BigDecimal("0.00");

            if (dueDate != null) {
                LocalDateTime dueDateTime = dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                LocalDateTime returnDateTime = returnTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                long daysBetween = ChronoUnit.DAYS.between(dueDateTime, returnDateTime);
                if (daysBetween > 0) {
                    overdueDays = (int) daysBetween;
                    isOverdue = 1;
                    fine = new BigDecimal(overdueDays).multiply(new BigDecimal("0.5")).setScale(2, java.math.RoundingMode.HALF_UP);
                }
            }

            // 更新Borrow表，更新结束时间和逾期信息
            Borrow borrow = new Borrow();
            borrow.setBorrowid(borrowid);
            borrow.setReturntime(returnTime);
            borrow.setIsoverdue(isOverdue);
            borrow.setOverduedays(overdueDays);
            borrow.setFine(fine);
            Integer res1 = borrowService.updateBorrow2(borrow);
            if (res1 == 0) throw new OperationFailureException("图书" + bookid + "更新借阅记录失败");

            // 构造返回结果
            result.put("status", 0);
            result.put("timestamp", System.currentTimeMillis());

            if (isOverdue == 1) {
                // 逾期还书：自动创建缴费记录
                int payResult = paymentService.pay(borrowid, theBorrow.getUserid());
                String fineMsg = "逾期 " + overdueDays + " 天，罚金 ¥" + fine;
                if (payResult == 2) {
                    result.put("message", "还书成功！（逾期已记录，" + fineMsg + "，缴费记录已存在）");
                } else if (payResult == 1) {
                    result.put("message", "还书成功！（逾期已记录，" + fineMsg + "，缴费记录已创建）");
                } else {
                    result.put("message", "还书成功！（逾期已记录，" + fineMsg + "，但缴费记录创建失败）");
                }
                // 将逾期详情放入 data
                java.util.Map<String, Object> data = new java.util.HashMap<>();
                data.put("overdueDays", overdueDays);
                data.put("fine", fine);
                data.put("isOverdue", 1);
                result.put("data", data);
            } else {
                result.put("message", "还书成功");
            }

        } catch (BookNotEnoughException e) {
            log.warn("还书业务异常", e);
            return R.getResultMap(1, e.getMessage());
        } catch (Exception e) {
            log.error("发生异常，进行手动回滚", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.getResultMap(1, "还书失败：" + e.getMessage());
        }
        return result;
    }

    // ============ 逾期管理 API ============

    /**
     * 分页查询所有逾期记录
     *
     * @param params {page, limit, username, bookname}
     * @return 逾期记录数据
     */
    @RequestMapping(value = "/overdue")
    public Map<String, Object> queryOverdue(@RequestParam Map<String, Object> params) {
        PageUtils.parsePageParams(params);
        int count = borrowService.getOverdueCount(params);
        List<Borrow> borrows = borrowService.searchOverdueByPage(params);
        return R.getListResultMap(0, "success", count, borrows);
    }

    /**
     * 查询当前仍在逾期中的记录（未还且超过应还日期）
     *
     * @return 当前逾期记录列表
     */
    @RequestMapping(value = "/overdue/current")
    public List<Borrow> queryCurrentOverdue() {
        return borrowService.getCurrentOverdue();
    }

    /**
     * 逾期统计数据
     *
     * @return 统计信息（当前逾期人数、总逾期次数、累计罚金）
     */
    @RequestMapping(value = "/statistics/overdue")
    public Map<String, Object> overdueStatistics() {
        return borrowService.getOverdueStatistics();
    }

    // ============ 续借与借书资格 API ============

    /**
     * 续借图书
     *
     * @param borrowId 借阅ID
     * @return 续借结果：1=成功，0=失败，2=已达续借上限，3=已逾期/已归还不可续借
     */
    @RequestMapping(value = {"/renew", "/reader/renew"})
    public Map<String, Object> renewBook(@RequestParam Integer borrowId) {
        int result = borrowService.renewBook(borrowId);
        switch (result) {
            case 1:
                return R.getResultMap(0, "续借成功，应还日期延长30天");
            case 2:
                return R.getResultMap(1, "已达续借上限（最多续借2次）");
            case 3:
                return R.getResultMap(1, "该书已逾期或已归还，不可续借");
            default:
                return R.getResultMap(1, "续借失败，记录不存在");
        }
    }

    /**
     * 检查读者借书资格
     *
     * @param userId 用户ID
     * @return 借书资格结果
     */
    @RequestMapping(value = {"/canBorrow", "/reader/canBorrow"})
    public Map<String, Object> canBorrow(@RequestParam Integer userId) {
        Map<String, Object> result = borrowService.checkUserCanBorrow(userId);
        boolean canBorrow = (boolean) result.get("canBorrow");
        if (canBorrow) {
            return R.getResultMap(0, "可以借书");
        } else {
            return R.getResultMap(1, (String) result.get("reason"), result);
        }
    }
}