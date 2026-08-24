package com.shanzhu.book.service.impl;

import com.shanzhu.book.mapper.BorrowMapper;
import com.shanzhu.book.mapper.PaymentMapper;
import com.shanzhu.book.model.Borrow;
import com.shanzhu.book.service.BorrowService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BorrowServiceImpl implements BorrowService {

    @Resource
    private BorrowMapper borrowMapper;

    @Resource
    private PaymentMapper paymentMapper;

    @Override
    public Integer getCount() {
        return borrowMapper.selectCount();
    }

    @Override
    public Integer getSearchCount(Map<String, Object> params) {
        return borrowMapper.selectCountBySearch(params);
    }

    @Override
    public List<Borrow> searchBorrowsByPage(Map<String, Object> params) {
        // 查询前先自动检测并更新所有未归还记录的逾期状态
        borrowMapper.updateOverdueStatus();
        List<Borrow> borrows = borrowMapper.selectBySearch(params);
        // 添加string类型的时间显示
        for(Borrow borrow : borrows) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(borrow.getBorrowtime() != null) borrow.setBorrowtimestr(simpleDateFormat.format(borrow.getBorrowtime()));
            if(borrow.getReturntime() != null) borrow.setReturntimestr(simpleDateFormat.format(borrow.getReturntime()));
            if(borrow.getDuedate() != null) borrow.setDuedatestr(simpleDateFormat.format(borrow.getDuedate()));
        }
        return borrows;
    }

    @Override
    public Integer addBorrow(Borrow borrow) {
        // 将string类型的时间重新调整
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            borrow.setBorrowtime(simpleDateFormat.parse(borrow.getBorrowtimestr()));
            borrow.setReturntime(simpleDateFormat.parse(borrow.getReturntimestr()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return borrowMapper.insertSelective(borrow);
    }

    // 不会调整时间格式的add
    @Override
    public Integer addBorrow2(Borrow borrow) {
        return borrowMapper.insertSelective(borrow);
    }

    @Override
    public Integer deleteBorrow(Borrow borrow) {
        // 先查询有没有还书
        Borrow borrow1 = borrowMapper.selectByPrimaryKey(borrow.getBorrowid());
        if(borrow1.getReturntime() == null) return 0;
        // 先删除关联的缴费记录，避免外键约束拦截
        paymentMapper.deleteByBorrowId(borrow.getBorrowid());
        return borrowMapper.deleteByPrimaryKey(borrow.getBorrowid());
    }

    @Override
    public Integer deleteBorrows(List<Borrow> borrows) {
        int count = 0;
        for(Borrow borrow : borrows) {
            count += deleteBorrow(borrow);
        }
        return count;
    }

    @Override
    public Integer updateBorrow(Borrow borrow) {
        // 将string类型的时间重新调整
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            borrow.setBorrowtime(simpleDateFormat.parse(borrow.getBorrowtimestr()));
            borrow.setReturntime(simpleDateFormat.parse(borrow.getReturntimestr()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return borrowMapper.updateByPrimaryKeySelective(borrow);
    }

    // 不调整时间格式的更新
    @Override
    public Integer updateBorrow2(Borrow borrow) {
        return borrowMapper.updateByPrimaryKeySelective(borrow);
    }

    @Override
    public Borrow queryBorrowsById(Integer borrowid) {
        return borrowMapper.selectByPrimaryKey(borrowid);
    }

    // ============ 逾期相关 ============

    @Override
    public Integer getOverdueCount(Map<String, Object> params) {
        return borrowMapper.selectCountOverdue(params);
    }

    @Override
    public List<Borrow> searchOverdueByPage(Map<String, Object> params) {
        List<Borrow> borrows = borrowMapper.selectOverdueByPage(params);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(Borrow borrow : borrows) {
            if(borrow.getBorrowtime() != null) borrow.setBorrowtimestr(simpleDateFormat.format(borrow.getBorrowtime()));
            if(borrow.getReturntime() != null) borrow.setReturntimestr(simpleDateFormat.format(borrow.getReturntime()));
            if(borrow.getDuedate() != null) borrow.setDuedatestr(simpleDateFormat.format(borrow.getDuedate()));
        }
        return borrows;
    }

    @Override
    public List<Borrow> getCurrentOverdue() {
        return borrowMapper.selectCurrentOverdue();
    }

    @Override
    public Map<String, Object> getOverdueStatistics() {
        return borrowMapper.selectOverdueStatistics();
    }

    // ============ 续借与借书资格 ============

    @Override
    public Integer renewBook(Integer borrowId) {
        Borrow borrow = borrowMapper.selectByPrimaryKey(borrowId);
        if (borrow == null) {
            return 0; // 记录不存在
        }

        // 检查是否已归还
        if (borrow.getReturntime() != null) {
            return 3; // 已归还，不可续借
        }

        // 检查续借次数
        int renewCount = borrow.getRenewcount() != null ? borrow.getRenewcount() : 0;
        if (renewCount >= 2) {
            return 2; // 已达续借上限
        }

        // 检查是否已逾期
        Date dueDate = borrow.getDuedate();
        if (dueDate != null && dueDate.before(new Date())) {
            return 3; // 已逾期不可续借
        }

        // 执行续借：renewCount+1，dueDate延长30天，更新lastRenewTime
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dueDate);
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date newDueDate = calendar.getTime();

        Borrow updateRecord = new Borrow();
        updateRecord.setBorrowid(borrowId);
        updateRecord.setRenewcount(renewCount + 1);
        updateRecord.setDuedate(newDueDate);
        updateRecord.setLastrenewwtime(now);

        int res = borrowMapper.updateRenew(updateRecord);
        return res > 0 ? 1 : 0;
    }

    @Override
    public Map<String, Object> checkUserCanBorrow(Integer userId) {
        Map<String, Object> result = new java.util.HashMap<>();

        // 查询逾期未归还的记录
        List<Borrow> overdueUnreturned = borrowMapper.selectUserOverdueUnreturned(userId);
        // 查询已归还但未缴费的逾期记录
        List<Borrow> unpaidOverdue = borrowMapper.selectUserUnpaidOverdue(userId);

        boolean canBorrow = overdueUnreturned.isEmpty() && unpaidOverdue.isEmpty();

        result.put("canBorrow", canBorrow);

        if (!canBorrow) {
            StringBuilder reason = new StringBuilder();
            if (!overdueUnreturned.isEmpty()) {
                reason.append("您有 ").append(overdueUnreturned.size())
                      .append(" 本书逾期未归还，请先归还图书并缴纳罚金。");
            }
            if (!unpaidOverdue.isEmpty()) {
                BigDecimal totalUnpaid = BigDecimal.ZERO;
                for (Borrow b : unpaidOverdue) {
                    if (b.getFine() != null) {
                        totalUnpaid = totalUnpaid.add(b.getFine());
                    }
                }
                if (reason.length() > 0) reason.append(" 同时");
                reason.append("您有 ").append(unpaidOverdue.size())
                      .append(" 笔未缴罚金，共计 ").append(totalUnpaid).append(" 元，请先缴费。");
            }
            result.put("reason", reason.toString());
            result.put("overdueUnreturned", overdueUnreturned);
            result.put("unpaidOverdue", unpaidOverdue);
        } else {
            result.put("reason", "");
        }

        return result;
    }

    @Override
    public Borrow queryActiveBorrow(Integer userId, Integer bookId) {
        return borrowMapper.selectActiveBorrowByUserAndBook(userId, bookId);
    }
}