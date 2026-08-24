package com.shanzhu.book.web;

import com.shanzhu.book.model.Payment;
import com.shanzhu.book.service.PaymentService;
import com.shanzhu.book.utils.PageUtils;
import com.shanzhu.book.utils.R;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 缴费管理 控制层
 *
 * @author: ShanZhu
 * @date: 2024-01-01
 */
@RestController
@RequestMapping(value = "/payment")
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    /**
     * 缴费
     *
     * @param params {borrowId, userId}
     * @return 缴费结果
     */
    @RequestMapping(value = "/pay")
    public Map<String, Object> pay(@RequestBody Map<String, Object> params) {
        Integer borrowId = toInt(params.get("borrowId"));
        Integer userId = toInt(params.get("userId"));
        if (borrowId == null || userId == null) {
            return R.getResultMap(1, "参数错误：缺少 borrowId 或 userId");
        }
        int result = paymentService.pay(borrowId, userId);
        switch (result) {
            case 1:
                return R.getResultMap(0, "缴费成功");
            case 2:
                return R.getResultMap(1, "该记录已缴费，无需重复");
            default:
                return R.getResultMap(1, "缴费失败，记录不存在或无罚金");
        }
    }

    private Integer toInt(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Integer) return (Integer) obj;
        if (obj instanceof Number) return ((Number) obj).intValue();
        try {
            return Integer.valueOf(obj.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 查询某读者的缴费记录列表
     *
     * @param userId 用户ID
     * @return 缴费记录列表
     */
    @RequestMapping(value = {"/list", "/reader/list"})
    public List<Payment> listByUserId(@RequestParam Integer userId) {
        return paymentService.getPaymentsByUserId(userId);
    }

    /**
     * 分页查询所有缴费记录（管理员）
     *
     * @param params {page, limit, username}
     * @return 缴费记录数据
     */
    @RequestMapping(value = "/queryPaymentsByPage")
    public Map<String, Object> queryPaymentsByPage(@RequestParam Map<String, Object> params) {
        PageUtils.parsePageParams(params);
        int count = paymentService.getPaymentCount(params);
        List<Payment> payments = paymentService.searchPaymentsByPage(params);
        return R.getListResultMap(0, "success", count, payments);
    }

    /**
     * 查询某读者未缴费的逾期记录列表
     *
     * @param userId 用户ID
     * @return 未缴费逾期记录
     */
    @RequestMapping(value = {"/unpaid", "/reader/unpaid"})
    public Map<String, Object> unpaid(@RequestParam Integer userId) {
        List<Map<String, Object>> list = paymentService.getUnpaidOverdueByUserId(userId);
        return R.getResultMap(0, "success", list);
    }
}