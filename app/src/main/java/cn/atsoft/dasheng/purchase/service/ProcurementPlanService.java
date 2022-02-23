package cn.atsoft.dasheng.purchase.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlan;
import cn.atsoft.dasheng.purchase.model.params.ProcurementPlanParam;
import cn.atsoft.dasheng.purchase.model.result.ProcurementPlanResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 采购计划主表 服务类
 * </p>
 *
 * @author song
 * @since 2021-12-21
 */
public interface ProcurementPlanService extends IService<ProcurementPlan> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-12-21
     */
    void add(ProcurementPlanParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-12-21
     */
    void delete(ProcurementPlanParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-12-21
     */
    void update(ProcurementPlanParam param);

    void updateState(ActivitiProcessTask processTask);

    void updateRefuseStatus(ActivitiProcessTask processTask);

    void detail(ProcurementPlanResult result);

    void updateStatus(Long planId) ;

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-12-21
     */
    ProcurementPlanResult findBySpec(ProcurementPlanParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-12-21
     */
    List<ProcurementPlanResult> findListBySpec(ProcurementPlanParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-12-21
     */
     PageInfo<ProcurementPlanResult> findPageBySpec(ProcurementPlanParam param);

    List<ProcurementPlanResult> listResultByIds(List<Long> ids);
}
