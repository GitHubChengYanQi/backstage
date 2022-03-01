package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Payment;
import cn.atsoft.dasheng.crm.model.params.PaymentParam;
import cn.atsoft.dasheng.crm.model.result.PaymentResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 付款信息表 服务类
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
public interface PaymentService extends IService<Payment> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-02-23
     */
    void add(PaymentParam param, String type);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-02-23
     */
    void delete(PaymentParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-02-23
     */
    void update(PaymentParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-02-23
     */
    PaymentResult findBySpec(PaymentParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-02-23
     */
    List<PaymentResult> findListBySpec(PaymentParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-02-23
     */
    PageInfo<PaymentResult> findPageBySpec(PaymentParam param);

    PaymentResult getDetail(Long orderId);
}
