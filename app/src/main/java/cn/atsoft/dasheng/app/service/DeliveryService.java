package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.app.model.result.OutstockRequest;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Delivery;
import cn.atsoft.dasheng.app.model.params.DeliveryParam;
import cn.atsoft.dasheng.app.model.result.DeliveryResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 发货表 服务类
 * </p>
 *
 * @author  
 * @since 2021-08-20
 */
public interface DeliveryService extends IService<Delivery> {

    /**
     * 新增
     *
     * @author  
     * @Date 2021-08-20
     */
    Long add(DeliveryParam param);

    /**
     * 删除
     *
     * @author  
     * @Date 2021-08-20
     */
    void delete(DeliveryParam param);

    /**
     * 更新
     *
     * @author  
     * @Date 2021-08-20
     */
    void update(DeliveryParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author  
     * @Date 2021-08-20
     */
    DeliveryResult findBySpec(DeliveryParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author  
     * @Date 2021-08-20
     */
    List<DeliveryResult> findListBySpec(DeliveryParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author  
     * @Date 2021-08-20
     */
     PageInfo findPageBySpec(DeliveryParam param, DataScope dataScope );

    /**
     * 增加发货详情
     * @param outstockRequest
     */
     void bulkShipment (OutstockRequest outstockRequest);

    void format(List<DeliveryResult> data);



}
