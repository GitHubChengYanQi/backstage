package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.bom.model.result.RestBomResult;
import cn.atsoft.dasheng.production.entity.ProductionTask;
import cn.atsoft.dasheng.production.model.params.ProductionTaskByCardParam;
import cn.atsoft.dasheng.production.model.params.ProductionTaskParam;
import cn.atsoft.dasheng.production.model.result.ProductionTaskResult;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.transaction.Transactional;
import java.util.List;

/**
 * <p>
 * 生产任务 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-22
 */
public interface ProductionTaskService extends IService<ProductionTask> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
    void add(ProductionTaskParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
    void delete(ProductionTaskParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
    ProductionTask update(ProductionTaskParam param);

    ProductionTask receive(ProductionTaskParam param);

    ProductionTask changeUser(ProductionTaskParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
    ProductionTaskResult findBySpec(ProductionTaskParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
    List<ProductionTaskResult> findListBySpec(ProductionTaskParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
     PageInfo<ProductionTaskResult> findPageBySpec(ProductionTaskParam param);

    void format(List<ProductionTaskResult> param);

    List<ProductionTaskResult> resultsByWorkOrderIds(List<Long> workOrderIds);

    List<ProductionTaskResult> resultsByIds(List<Long> taskIds);

    void createTaskByBom(Long bomId, Integer num, String source, Long sourceId, Long cardId);

    void createTaskByBom(Long bomId, Integer num, Long cardId);

    void createTaskByBom(ProductionTaskByCardParam param);

    @Transactional
    void doneTasks(ProductionTaskByCardParam param);

    List<RestBomResult> formatBomList(List<ProductionTaskResult> productionTaskResults);
}
