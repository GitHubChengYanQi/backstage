package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityPlanDetail;
import cn.atsoft.dasheng.erp.model.params.QualityPlanDetailParam;
import cn.atsoft.dasheng.erp.model.result.QualityPlanDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 质检方案详情 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-10-28
 */
public interface QualityPlanDetailService extends IService<QualityPlanDetail> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    void add(QualityPlanDetailParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    void delete(QualityPlanDetailParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    void update(QualityPlanDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    QualityPlanDetailResult findBySpec(QualityPlanDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    List<QualityPlanDetailResult> findListBySpec(QualityPlanDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
     PageInfo<QualityPlanDetailResult> findPageBySpec(QualityPlanDetailParam param);

}
