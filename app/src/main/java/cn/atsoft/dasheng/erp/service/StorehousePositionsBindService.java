package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.StorehousePositionsBind;
import cn.atsoft.dasheng.erp.model.params.StorehousePositionsBindParam;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsBindResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 库位绑定物料表 服务类
 * </p>
 *
 * @author song
 * @since 2022-01-20
 */
public interface StorehousePositionsBindService extends IService<StorehousePositionsBind> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-01-20
     */
    StorehousePositionsBind add(StorehousePositionsBindParam param);

    void bindBatch(StorehousePositionsBindParam param);
    void SpuAddBind(StorehousePositionsBindParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-01-20
     */
    void delete(StorehousePositionsBindParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-01-20
     */
    void update(StorehousePositionsBindParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-01-20
     */
    StorehousePositionsBindResult findBySpec(StorehousePositionsBindParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-01-20
     */
    List<StorehousePositionsBindResult> findListBySpec(StorehousePositionsBindParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-01-20
     */
    PageInfo<StorehousePositionsBindResult> findPageBySpec(StorehousePositionsBindParam param);


}
