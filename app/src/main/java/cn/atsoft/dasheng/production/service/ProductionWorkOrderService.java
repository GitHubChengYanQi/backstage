package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionCard;
import cn.atsoft.dasheng.production.entity.ProductionWorkOrder;
import cn.atsoft.dasheng.production.model.params.ProductionWorkOrderParam;
import cn.atsoft.dasheng.production.model.result.ProductionWorkOrderResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 工单 服务类
 * </p>
 *
 * @author 
 * @since 2022-02-28
 */
public interface ProductionWorkOrderService extends IService<ProductionWorkOrder> {

    /**
     * 新增
     *
     * @author 
     * @Date 2022-02-28
     */
    void add(ProductionWorkOrderParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2022-02-28
     */
    void delete(ProductionWorkOrderParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2022-02-28
     */
    void update(ProductionWorkOrderParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2022-02-28
     */
    ProductionWorkOrderResult findBySpec(ProductionWorkOrderParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2022-02-28
     */
    List<ProductionWorkOrderResult> findListBySpec(ProductionWorkOrderParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2022-02-28
     */
     PageInfo<ProductionWorkOrderResult> findPageBySpec(ProductionWorkOrderParam param);


    void microServiceAdd(Object param, List<ProductionCard> cardList);

    List<ProductionWorkOrderResult> resultsBySourceIds(String source, List<Long> sourceIds);
}
