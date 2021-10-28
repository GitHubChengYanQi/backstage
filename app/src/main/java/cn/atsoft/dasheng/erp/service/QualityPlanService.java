package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityPlan;
import cn.atsoft.dasheng.erp.model.params.QualityPlanParam;
import cn.atsoft.dasheng.erp.model.result.QualityPlanResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 质检方案 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-10-28
 */
public interface QualityPlanService extends IService<QualityPlan> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    void add(QualityPlanParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    void delete(QualityPlanParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    void update(QualityPlanParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    QualityPlanResult findBySpec(QualityPlanParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    List<QualityPlanResult> findListBySpec(QualityPlanParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
     PageInfo<QualityPlanResult> findPageBySpec(QualityPlanParam param);

}
