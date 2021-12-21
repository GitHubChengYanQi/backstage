package cn.atsoft.dasheng.purchase.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlanDetal;
import cn.atsoft.dasheng.purchase.model.params.ProcurementPlanDetalParam;
import cn.atsoft.dasheng.purchase.model.params.ProcurementPlanParam;
import cn.atsoft.dasheng.purchase.model.result.ProcurementPlanDetalResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 采购计划单子表  整合数据后的 子表 服务类
 * </p>
 *
 * @author song
 * @since 2021-12-21
 */
public interface ProcurementPlanDetalService extends IService<ProcurementPlanDetal> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-12-21
     */
    void add(ProcurementPlanDetalParam param);

    void batchAdd(ProcurementPlanParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-12-21
     */
    void delete(ProcurementPlanDetalParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-12-21
     */
    void update(ProcurementPlanDetalParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-12-21
     */
    ProcurementPlanDetalResult findBySpec(ProcurementPlanDetalParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-12-21
     */
    List<ProcurementPlanDetalResult> findListBySpec(ProcurementPlanDetalParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-12-21
     */
     PageInfo<ProcurementPlanDetalResult> findPageBySpec(ProcurementPlanDetalParam param);

}
