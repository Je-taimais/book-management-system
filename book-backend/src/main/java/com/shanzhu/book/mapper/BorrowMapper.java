package com.shanzhu.book.mapper;

import com.shanzhu.book.model.Borrow;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BorrowMapper {
    int deleteByPrimaryKey(Integer borrowid);

    int insert(Borrow record);

    int insertSelective(Borrow record);

    Borrow selectByPrimaryKey(Integer borrowid);

    int updateByPrimaryKeySelective(Borrow record);

    int updateByPrimaryKey(Borrow record);

    List<Borrow> selectAllByLimit(@Param("begin") Integer begin, @Param("size") Integer size);

    Integer selectCount();

    int selectCountBySearch(Map<String, Object> searchParam);

    List<Borrow> selectBySearch(Map<String, Object> searchParam);

    Integer selectCountByReader(Integer userid);

    List<Borrow> selectAllByLimitByReader(@Param("begin") Integer begin, @Param("size") Integer size, @Param("userid") Integer userid);

    // ============ 逾期相关 ============

    int selectCountOverdue(Map<String, Object> searchParam);

    List<Borrow> selectOverdueByPage(Map<String, Object> searchParam);

    List<Borrow> selectCurrentOverdue();

    Map<String, Object> selectOverdueStatistics();

    // ============ 逾期状态自动更新 ============

    int updateOverdueStatus();

    // ============ 续借与借书资格 ============

    int updateRenew(Borrow record);

    List<Borrow> selectUserOverdueUnreturned(@Param("userid") Integer userid);

    List<Borrow> selectUserUnpaidOverdue(@Param("userid") Integer userid);

    /**
     * 查询某用户是否已借阅某本书且未归还（防止重复借阅）
     */
    Borrow selectActiveBorrowByUserAndBook(@Param("userid") Integer userid, @Param("bookid") Integer bookid);
}