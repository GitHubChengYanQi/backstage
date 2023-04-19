package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.model.params.StorehouseParam;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
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
public interface StorehouseService extends IService<Storehouse> {

    /**
     * 新增
     *
     * @author
     * @Date 2021-07-15
     */
    Long add(StorehouseParam param);

    /**
     * 删除
     *
     * @author
     * @Date 2021-07-15
     */
    void delete(StorehouseParam param);

    /**
     * 更新
     *
     * @author
     * @Date 2021-07-15
     */
    Storehouse update(StorehouseParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author
     * @Date 2021-07-15
     */
    StorehouseResult findBySpec(StorehouseParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author
     * @Date 2021-07-15
     */
    List<StorehouseResult> findListBySpec(StorehouseParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author
     * @Date 2021-07-15
     */
    PageInfo findPageBySpec(StorehouseParam param, DataScope dataScope);


    StorehouseResult getDetail(Long Id);

    List<StorehouseResult> getDetails(List<Long> ids);
}
