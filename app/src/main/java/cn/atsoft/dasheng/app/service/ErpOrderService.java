package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ErpOrder;
import cn.atsoft.dasheng.app.model.params.ErpOrderParam;
import cn.atsoft.dasheng.app.model.result.ErpOrderResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author ta
 * @since 2021-07-26
 */
public interface ErpOrderService extends IService<ErpOrder> {

    /**
     * 新增
     *
     * @author ta
     * @Date 2021-07-26
     */
    void add(ErpOrderParam param);

    /**
     * 删除
     *
     * @author ta
     * @Date 2021-07-26
     */
    void delete(ErpOrderParam param);

    /**
     * 更新
     *
     * @author ta
     * @Date 2021-07-26
     */
    void update(ErpOrderParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author ta
     * @Date 2021-07-26
     */
    ErpOrderResult findBySpec(ErpOrderParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author ta
     * @Date 2021-07-26
     */
    List<ErpOrderResult> findListBySpec(ErpOrderParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author ta
     * @Date 2021-07-26
     */
     PageInfo<ErpOrderResult> findPageBySpec(ErpOrderParam param);

}
