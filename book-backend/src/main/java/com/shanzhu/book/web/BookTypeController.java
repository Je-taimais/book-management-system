package com.shanzhu.book.web;

import com.shanzhu.book.model.BookType;
import com.shanzhu.book.service.BookTypeService;
import com.shanzhu.book.utils.PageUtils;
import com.shanzhu.book.utils.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 图书类型 控制层
 *
 * @author: ShanZhu
 * @date: 2023-12-31
 */
@RestController
@RequestMapping(value = "/bookType")
public class BookTypeController {

    @Resource
    private BookTypeService bookTypeService;

    /**
     * 获得数量
     *
     * @return 数量
     */
    @GetMapping(value = "/getCount")
    public Integer getCount(){
        return bookTypeService.getCount();
    }

    /**
     * 查询所有图书类型
     *
     * @return 图书类型
     */
    @GetMapping(value = {"/queryBookTypes", "/reader/queryBookTypes"})
    public List<BookType> queryBookTypes(){
        return bookTypeService.queryBookTypes();
    }

    /**
     * 分页查询图书类型
     *
     * @param params {page, limit, booktypename}
     * @return 图书类型
     */
    @GetMapping(value = "/queryBookTypesByPage")
    public Map<String, Object> queryBookTypesByPage(@RequestParam Map<String, Object> params){
        PageUtils.parsePageParams(params);
        int count = bookTypeService.getSearchCount(params);
        List<BookType> bookTypes = bookTypeService.searchBookTypesByPage(params);
        return R.getListResultMap(0, "success", count, bookTypes);
    }

    /**
     * 添加类型
     *
     * @param bookType 图书类型
     * @return 结果
     */
    @PostMapping(value = "/addBookType")
    public Integer addBookType(@RequestBody BookType bookType){
        return bookTypeService.addBookType(bookType);
    }

    /**
     * 删除类型
     *
     * @param bookType 图书类型
     * @return 结果
     */
    @DeleteMapping(value = "/deleteBookType")
    public Integer deleteBookType(@RequestBody BookType bookType){
        return bookTypeService.deleteBookType(bookType);
    }

    /**
     * 批量删除类型
     *
     * @param bookTypes 图书类型
     * @return 结果
     */
    @DeleteMapping(value = "/deleteBookTypes")
    public Integer deleteBookTypes(@RequestBody List<BookType> bookTypes){
        return bookTypeService.deleteBookTypes(bookTypes);
    }

    /**
     * 更新类型
     *
     * @param bookType 图书类型
     * @return 结果
     */
    @PutMapping(value = "/updateBookType")
    public Integer updateBookType(@RequestBody BookType bookType){
        return bookTypeService.updateBookType(bookType);
    }

}
