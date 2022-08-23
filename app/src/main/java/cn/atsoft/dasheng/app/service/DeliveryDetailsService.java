package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.app.model.result.CrmBusinessSalesResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.DeliveryDetails;
import cn.atsoft.dasheng.app.model.params.DeliveryDetailsParam;
import cn.atsoft.dasheng.app.model.result.DeliveryDetailsResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author  
 * @since 2021-08-20
 */
public interface DeliveryDetailsService extends IService<DeliveryDetails> {

    /**
     * 新增
     *
     * @author  
     * @Date 2021-08-20
     */
    DeliveryDetails add(DeliveryDetailsParam param);

    /**
     * 删除
     *
     * @author  
     * @Date 2021-08-20
     */
    void delete(DeliveryDetailsParam param);

    /**
     * 更新
     *
     * @author  
     * @Date 2021-08-20
     */
    void update(DeliveryDetailsParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author  
     * @Date 2021-08-20
     */
    DeliveryDetailsResult findBySpec(DeliveryDetailsParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author  
     * @Date 2021-08-20
     */
    List<DeliveryDetailsResult> findListBySpec(DeliveryDetailsParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author  
     * @Date 2021-08-20
     */
     PageInfo findPageBySpec(DeliveryDetailsParam param, DataScope dataScope );

    List<DeliveryDetailsResult> getByIds(List<Long> ids);

    DeliveryDetailsResult format(List<DeliveryDetailsResult> data);


}
