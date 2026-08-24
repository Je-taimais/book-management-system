package com.shanzhu.book.mapper;

import com.shanzhu.book.model.BookInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 图书信息 持久层
 *
 * @author: ShanZhu
 * @date: 2023-12-31
 */
public interface BookInfoMapper {

    /**
     * 通过主键id删除
     *
     * @param bookid 图书id
     * @return 结果
     */
    int deleteByPrimaryKey(Integer bookid);

    int insert(BookInfo record);

    int insertSelective(BookInfo record);

    BookInfo selectByPrimaryKey(Integer bookid);

    int updateByPrimaryKeySelective(BookInfo record);

    int updateByPrimaryKey(BookInfo record);

    List<BookInfo> selectAllByLimit(@Param("begin") Integer begin, @Param("size") Integer size);


    Integer selectCount();

    int selectCountBySearch(Map<String, Object> searchParam);

    List<BookInfo> selectBySearch(Map<String, Object> searchParam);

    List<BookInfo> selectAll();

    int selectCountByType(Map<String, Object> map);

    List<BookInfo> selectByType(Map<String, Object> map);
}
