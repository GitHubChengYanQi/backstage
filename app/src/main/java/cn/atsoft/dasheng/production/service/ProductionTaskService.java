package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionTask;
import cn.atsoft.dasheng.production.model.params.ProductionTaskParam;
import cn.atsoft.dasheng.production.model.result.ProductionTaskResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 生产任务 服务类
 * </p>
 *
 * @author 
 * @since 2022-02-28
 */
public interface ProductionTaskService extends IService<ProductionTask> {

    /**
     * 新增
     *
     * @author 
     * @Date 2022-02-28
     */
    void add(ProductionTaskParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2022-02-28
     */
    void delete(ProductionTaskParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2022-02-28
     */
    void update(ProductionTaskParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2022-02-28
     */
    ProductionTaskResult findBySpec(ProductionTaskParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2022-02-28
     */
    List<ProductionTaskResult> findListBySpec(ProductionTaskParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2022-02-28
     */
     PageInfo<ProductionTaskResult> findPageBySpec(ProductionTaskParam param);

}