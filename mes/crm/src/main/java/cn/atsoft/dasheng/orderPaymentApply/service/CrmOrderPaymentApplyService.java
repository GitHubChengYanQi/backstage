package cn.atsoft.dasheng.orderPaymentApply.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.orderPaymentApply.entity.CrmOrderPaymentApply;
import cn.atsoft.dasheng.orderPaymentApply.model.params.CrmOrderPaymentApplyParam;
import cn.atsoft.dasheng.orderPaymentApply.model.result.CrmOrderPaymentApplyResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-18
 */
public interface CrmOrderPaymentApplyService extends IService<CrmOrderPaymentApply> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    void add(CrmOrderPaymentApplyParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    void delete(CrmOrderPaymentApplyParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    void update(CrmOrderPaymentApplyParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    CrmOrderPaymentApplyResult findBySpec(CrmOrderPaymentApplyParam param);

    CrmOrderPaymentApplyResult detail(CrmOrderPaymentApplyParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
    List<CrmOrderPaymentApplyResult> findListBySpec(CrmOrderPaymentApplyParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-03-18
     */
     PageInfo<CrmOrderPaymentApplyResult> findPageBySpec(DataScope dataScope, CrmOrderPaymentApplyParam param);

}
