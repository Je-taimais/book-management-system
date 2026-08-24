package com.shanzhu.book.model;

import lombok.Data;

/**
 * 地址相关
 *
 * @author: ShanZhu
 * @date: 2023-12-31
 */
@Data
public class BookType {

    /**
     * 图书类型id
     */
    private Integer booktypeid;

    /**
     * 图书类型名
     */
    private String booktypename;

    /**
     * 图书类型描述
     */
    private String booktypedesc;

}