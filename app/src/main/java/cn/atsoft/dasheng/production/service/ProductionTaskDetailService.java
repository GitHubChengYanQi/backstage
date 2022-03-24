package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionTaskDetail;
import cn.atsoft.dasheng.production.model.params.ProductionTaskDetailParam;
import cn.atsoft.dasheng.production.model.result.ProductionTaskDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-22
 */
public interface ProductionTaskDetailService extends IService<ProductionTaskDetail> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
    void add(ProductionTaskDetailParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
    void delete(ProductionTaskDetailParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
    void update(ProductionTaskDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
    ProductionTaskDetailResult findBySpec(ProductionTaskDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
    List<ProductionTaskDetailResult> findListBySpec(ProductionTaskDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
     PageInfo<ProductionTaskDetailResult> findPageBySpec(ProductionTaskDetailParam param);

    List<ProductionTaskDetailResult> resultsByTaskId(Long taskId);
}
