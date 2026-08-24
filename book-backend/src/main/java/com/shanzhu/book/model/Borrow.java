package com.shanzhu.book.model;

import lombok.Data;

import java.util.Date;

/**
 * 借阅信息
 *
 * @author: ShanZhu
 * @date: 2023-12-31
 */
@Data
public class Borrow {

    /**
     * id
     */
    private Integer borrowid;

    /**
     * 借阅者用户id
     */
    private Integer userid;

    /**
     * 借阅者名称
     */
    private String username;

    /**
     * 借阅图书id
     */
    private Integer bookid;

    /**
     * 借阅图书名
     */
    private String bookname;

    /**
     * 借阅时间
     */
    private Date borrowtime;

    /**
     * 借阅时间str
     */
    private String borrowtimestr;

    /**
     * 归还时间
     */
    private Date returntime;

    /**
     * 归还时间str
     */
    private String returntimestr;

    /**
     * 应还日期
     */
    private Date duedate;

    /**
     * 应还日期str
     */
    private String duedatestr;

    /**
     * 是否逾期，0否1是
     */
    private Integer isoverdue;

    /**
     * 逾期天数
     */
    private Integer overduedays;

    /**
     * 罚金（每天0.5元）
     */
    private java.math.BigDecimal fine;

    /**
     * 已续借次数（最多2次）
     */
    private Integer renewcount;

    /**
     * 最后一次续借时间
     */
    private Date lastrenewwtime;

    /**
     * 最后一次续借时间str
     */
    private String lastrenewwtimestr;

}
