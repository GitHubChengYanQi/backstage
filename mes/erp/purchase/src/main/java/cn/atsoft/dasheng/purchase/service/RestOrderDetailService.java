package cn.atsoft.dasheng.purchase.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.RestOrderDetail;
import cn.atsoft.dasheng.purchase.model.params.RestOrderDetailParam;
import cn.atsoft.dasheng.purchase.model.result.RestOrderDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单明细表 服务类
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
public interface RestOrderDetailService extends IService<RestOrderDetail> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-02-23
     */
    void add(RestOrderDetailParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-02-23
     */
    void delete(RestOrderDetailParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-02-23
     */
    void update(RestOrderDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-02-23
     */
    RestOrderDetailResult findBySpec(RestOrderDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-02-23
     */
    List<RestOrderDetailResult> findListBySpec(RestOrderDetailParam param);

    List<RestOrderDetailResult> historyList(RestOrderDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-02-23
     */
    PageInfo<RestOrderDetailResult> findPageBySpec(RestOrderDetailParam param);


    void format(List<RestOrderDetailResult> param);
}
