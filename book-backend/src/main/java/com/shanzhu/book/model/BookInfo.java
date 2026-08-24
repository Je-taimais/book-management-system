package com.shanzhu.book.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 图书信息
 *
 * @author: ShanZhu
 * @date: 2023-12-31
 */
@Data
public class BookInfo {

    /**
     * 图书id
     */
    private Integer bookid;

    /**
     * 书名
     */
    private String bookname;

    /**
     * 作者
     */
    private String bookauthor;

    /**
     * 价格
     */
    private BigDecimal bookprice;

    /**
     * 图书类型id
     */
    private Integer booktypeid;

    /**
     * 图书类型名
     */
    private String booktypename;

    /**
     * 图书描述
     */
    private String bookdesc;

    /**
     * 是否借阅中
     */
    private Byte isborrowed;

    /**
     * 库存数量（可借数量）
     */
    private Integer stock;

    /**
     * 图书封面
     */
    private String bookimg;

}
