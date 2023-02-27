package cn.atsoft.dasheng.purchase.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.RestOrder;
import cn.atsoft.dasheng.purchase.model.params.RestOrderParam;
import cn.atsoft.dasheng.purchase.model.result.RestOrderResult;
import cn.atsoft.dasheng.purchase.model.result.RestOrderSimpleResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
public interface RestOrderService extends IService<RestOrder> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-02-23
     */
    RestOrder add(RestOrderParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-02-23
     */
    void delete(RestOrderParam param);


    /**
     * 更新
     *
     * @author song
     * @Date 2022-02-23
     */
    void update(RestOrderParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-02-23
     */
    RestOrderResult findBySpec(RestOrderParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-02-23
     */
    List<RestOrderResult> findListBySpec(RestOrderParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-02-23
     */
    PageInfo<RestOrderResult> findPageBySpec(RestOrderParam param);


    PageInfo<RestOrderSimpleResult> simpleFindPageBySpec(RestOrderParam param);



    void format(List<RestOrderResult> data);
}
