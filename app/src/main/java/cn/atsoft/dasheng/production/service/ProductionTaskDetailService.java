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
 * @author 
 * @since 2022-02-28
 */
public interface ProductionTaskDetailService extends IService<ProductionTaskDetail> {

    /**
     * 新增
     *
     * @author 
     * @Date 2022-02-28
     */
    void add(ProductionTaskDetailParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2022-02-28
     */
    void delete(ProductionTaskDetailParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2022-02-28
     */
    void update(ProductionTaskDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2022-02-28
     */
    ProductionTaskDetailResult findBySpec(ProductionTaskDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2022-02-28
     */
    List<ProductionTaskDetailResult> findListBySpec(ProductionTaskDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2022-02-28
     */
     PageInfo<ProductionTaskDetailResult> findPageBySpec(ProductionTaskDetailParam param);

}
