package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.model.params.StorehousePositionsParam;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 仓库库位表 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
public interface StorehousePositionsService extends IService<StorehousePositions> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-29
     */
    void add(StorehousePositionsParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-29
     */
    void delete(StorehousePositionsParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-29
     */
    void update(StorehousePositionsParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-29
     */
    StorehousePositionsResult findBySpec(StorehousePositionsParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-29
     */
    List<StorehousePositionsResult> findListBySpec(StorehousePositionsParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-29
     */
     PageInfo<StorehousePositionsResult> findPageBySpec(StorehousePositionsParam param);

}
