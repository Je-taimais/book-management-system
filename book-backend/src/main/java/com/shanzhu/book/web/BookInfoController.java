package com.shanzhu.book.web;

import com.shanzhu.book.model.BookInfo;
import com.shanzhu.book.service.BookInfoService;
import com.shanzhu.book.utils.PageUtils;
import com.shanzhu.book.utils.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 图书信息 控制层
 *
 * @author: ShanZhu
 * @date: 2023-12-31
 */
@RestController
@RequestMapping(value = "/bookInfo")
public class BookInfoController {

    @Resource
    private BookInfoService bookInfoService;

    /**
     * 获取图书数量
     *
     * @return 图书数量
     */
    @GetMapping(value = "/getCount")
    public Integer getCount() {
        return bookInfoService.getCount();
    }

    /**
     * 查询所有图书列表
     *
     * @return 图书列表
     */
    @GetMapping(value = "/queryBookInfos")
    public List<BookInfo> queryBookInfos() {
        return bookInfoService.queryBookInfos();
    }

    /**
     * 分页搜索查询图书信息
     *
     * @param params {page, limit, bookname, bookauthor, booktypeid}
     * @return 图书信息
     */
    @GetMapping(value = "/queryBookInfosByPage")
    public Map<String, Object> queryBookInfosByPage(@RequestParam Map<String, Object> params) {
        PageUtils.parsePageParams(params);
        // 获得总数
        int count = bookInfoService.getSearchCount(params);
        // 分页查询
        List<BookInfo> bookInfos = bookInfoService.searchBookInfosByPage(params);
        return R.getListResultMap(0, "success", count, bookInfos);
    }

    /**
     * 添加图书
     *
     * @param bookInfo 图书信息
     * @return 结果
     */
    @PostMapping(value = "/addBookInfo")
    public Integer addBookInfo(@RequestBody BookInfo bookInfo) {
        return bookInfoService.addBookInfo(bookInfo);
    }

    /**
     * 删除图书
     *
     * @param bookInfo 图书信息
     * @return 结果
     */
    @DeleteMapping(value = "/deleteBookInfo")
    public Integer deleteBookInfo(@RequestBody BookInfo bookInfo) {
        return bookInfoService.deleteBookInfo(bookInfo);
    }

    /**
     * 批量删除图书
     *
     * @param bookInfos 图书信息
     * @return 结果
     */
    @DeleteMapping(value = "/deleteBookInfos")
    public Integer deleteBookInfos(@RequestBody List<BookInfo> bookInfos) {
        return bookInfoService.deleteBookInfos(bookInfos);
    }

    /**
     * 更新图书
     *
     * @param bookInfo 图书信息
     * @return 结果
     */
    @PutMapping(value = "/updateBookInfo")
    public Integer updateBookInfo(@RequestBody BookInfo bookInfo) {
        return bookInfoService.updateBookInfo(bookInfo);
    }
}
