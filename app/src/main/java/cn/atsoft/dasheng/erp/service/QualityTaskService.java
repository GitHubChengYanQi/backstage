package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.model.params.QualityTaskParam;
import cn.atsoft.dasheng.erp.model.result.QualityTaskResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 质检任务 服务类
 * </p>
 *
 * @author 
 * @since 2021-11-16
 */
public interface QualityTaskService extends IService<QualityTask> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-11-16
     */
    void add(QualityTaskParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-11-16
     */
    void delete(QualityTaskParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-11-16
     */
    void update(QualityTaskParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-11-16
     */
    QualityTaskResult findBySpec(QualityTaskParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-11-16
     */
    List<QualityTaskResult> findListBySpec(QualityTaskParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-11-16
     */
     PageInfo<QualityTaskResult> findPageBySpec(QualityTaskParam param);

}
