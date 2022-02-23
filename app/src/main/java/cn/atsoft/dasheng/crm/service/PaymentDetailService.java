package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.PaymentDetail;
import cn.atsoft.dasheng.crm.model.params.PaymentDetailParam;
import cn.atsoft.dasheng.crm.model.result.PaymentDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 付款详情 服务类
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
public interface PaymentDetailService extends IService<PaymentDetail> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-02-23
     */
    void add(PaymentDetailParam param);

    void addList(Long paymentId, List<PaymentDetailParam> params);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-02-23
     */
    void delete(PaymentDetailParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-02-23
     */
    void update(PaymentDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-02-23
     */
    PaymentDetailResult findBySpec(PaymentDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-02-23
     */
    List<PaymentDetailResult> findListBySpec(PaymentDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-02-23
     */
     PageInfo<PaymentDetailResult> findPageBySpec(PaymentDetailParam param);

    List<PaymentDetailResult> getResults(Long paymentId);
}
