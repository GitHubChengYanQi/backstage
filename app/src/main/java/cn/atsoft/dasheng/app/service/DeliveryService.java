package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Delivery;
import cn.atsoft.dasheng.app.model.params.DeliveryParam;
import cn.atsoft.dasheng.app.model.result.DeliveryResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 出库表 服务类
 * </p>
 *
 * @author song
 * @since 2021-07-17
 */
public interface DeliveryService extends IService<Delivery> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-07-17
     */
    void add(DeliveryParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-07-17
     */
    void delete(DeliveryParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-07-17
     */
    void update(DeliveryParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-07-17
     */
    DeliveryResult findBySpec(DeliveryParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-07-17
     */
    List<DeliveryResult> findListBySpec(DeliveryParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-07-17
     */
     PageInfo<DeliveryResult> findPageBySpec(DeliveryParam param);

}
