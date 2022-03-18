package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.app.model.params.OutstockParam;
import cn.atsoft.dasheng.app.pojo.FreeOutStockParam;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.OutstockOrder;
import cn.atsoft.dasheng.app.model.params.OutstockOrderParam;
import cn.atsoft.dasheng.app.model.result.OutstockOrderResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.erp.model.params.OutstockListingParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 出库单 服务类
 * </p>
 *
 * @author cheng
 * @since 2021-08-16
 */
public interface OutstockOrderService extends IService<OutstockOrder> {

    /**
     * 新增
     *
     * @author cheng
     * @Date 2021-08-16
     */
    OutstockOrder add(OutstockOrderParam param);

    void AkeyOutbound(OutstockOrderParam param);

    OutstockOrderResult getOrder(Long id);

    void outBound(List<OutstockListingParam> listings);

    /**
     * 删除
     *
     * @author cheng
     * @Date 2021-08-16
     */
    void delete(OutstockOrderParam param);

    /**
     * 更新
     *
     * @author cheng
     * @Date 2021-08-16
     */
    OutstockOrder update(OutstockOrderParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author cheng
     * @Date 2021-08-16
     */
    OutstockOrderResult findBySpec(OutstockOrderParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author cheng
     * @Date 2021-08-16
     */
    List<OutstockOrderResult> findListBySpec(OutstockOrderParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author cheng
     * @Date 2021-08-16
     */
    PageInfo<OutstockOrderResult> findPageBySpec(OutstockOrderParam param, DataScope dataScope);



    /**
     * 自由出库
     *
     * @param freeOutStockParam
     */
    void freeOutStock(FreeOutStockParam freeOutStockParam);

}
