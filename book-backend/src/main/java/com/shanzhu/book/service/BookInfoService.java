package com.shanzhu.book.service;

import com.shanzhu.book.model.BookInfo;

import java.util.List;
import java.util.Map;

/**
 * 图书信息 服务层
 *
 * @author: ShanZhu
 * @date: 2023-12-31
 */
public interface BookInfoService {

    /**
     * 获取图书数量
     *
     * @return 图书数量
     */
    Integer getCount();

    /**
     * 查询所有图书列表
     *
     * @return 图书列表
     */
    List<BookInfo> queryBookInfos();

    BookInfo queryBookInfoById(Integer bookid);

    /**
     * 分页搜索查询图书总数
     *
     * @param params {page, limit, bookname, bookauthor, booktypeid}
     * @return 图书信息
     */
    Integer getSearchCount(Map<String, Object> params);

    /**
     * 分页搜索查询图书
     *
     * @param params {page, limit, bookname, bookauthor, booktypeid}
     * @return 图书信息
     */
    List<BookInfo> searchBookInfosByPage(Map<String, Object> params);

    /**
     * 添加图书
     *
     * @param bookInfo 图书信息
     * @return 结果
     */
    Integer addBookInfo(BookInfo bookInfo);

    /**
     * 删除图书
     *
     * @param bookInfo 图书信息
     * @return 结果
     */
    Integer deleteBookInfo(BookInfo bookInfo);

    /**
     * 批量删除图书
     *
     * @param bookInfos 图书信息
     * @return 结果
     */
    Integer deleteBookInfos(List<BookInfo> bookInfos);

    /**
     * 更新图书
     *
     * @param bookInfo 图书信息
     * @return 结果
     */
    Integer updateBookInfo(BookInfo bookInfo);
}
