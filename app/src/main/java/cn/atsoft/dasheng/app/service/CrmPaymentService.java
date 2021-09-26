package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmPayment;
import cn.atsoft.dasheng.app.model.params.CrmPaymentParam;
import cn.atsoft.dasheng.app.model.result.CrmPaymentResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 付款信息表 服务类
 * </p>
 *
 * @author ta
 * @since 2021-07-26
 */
public interface CrmPaymentService extends IService<CrmPayment> {

    /**
     * 新增
     *
     * @author ta
     * @Date 2021-07-26
     */
    void add(CrmPaymentParam param);

    /**
     * 删除
     *
     * @author ta
     * @Date 2021-07-26
     */
    void delete(CrmPaymentParam param);

    /**
     * 更新
     *
     * @author ta
     * @Date 2021-07-26
     */
    void update(CrmPaymentParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author ta
     * @Date 2021-07-26
     */
    CrmPaymentResult findBySpec(CrmPaymentParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author ta
     * @Date 2021-07-26
     */
    List<CrmPaymentResult> findListBySpec(CrmPaymentParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author ta
     * @Date 2021-07-26
     */
     PageInfo<CrmPaymentResult> findPageBySpec(CrmPaymentParam param, DataScope dataScope );

}
