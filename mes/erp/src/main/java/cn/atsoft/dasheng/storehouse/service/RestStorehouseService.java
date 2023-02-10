package cn.atsoft.dasheng.storehouse.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.storehouse.entity.RestStorehouse;
import cn.atsoft.dasheng.storehouse.model.params.RestStorehouseParam;
import cn.atsoft.dasheng.storehouse.model.result.RestStorehouseResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 地点表 服务类
 * </p>
 *
 * @author
 * @since 2021-07-15
 */
public interface RestStorehouseService extends IService<RestStorehouse> {

    /**
     * 新增
     *
     * @author
     * @Date 2021-07-15
     */
    Long add(RestStorehouseParam param);

    /**
     * 删除
     *
     * @author
     * @Date 2021-07-15
     */
    void delete(RestStorehouseParam param);

    /**
     * 更新
     *
     * @author
     * @Date 2021-07-15
     */
    void update(RestStorehouseParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author
     * @Date 2021-07-15
     */
    RestStorehouseResult findBySpec(RestStorehouseParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author
     * @Date 2021-07-15
     */
    List<RestStorehouseResult> findListBySpec(RestStorehouseParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author
     * @Date 2021-07-15
     */
    PageInfo findPageBySpec(RestStorehouseParam param, DataScope dataScope);


    RestStorehouseResult getDetail(Long Id);

    List<RestStorehouseResult> getDetails(List<Long> ids);
}
