package cn.atsoft.dasheng.storehouse.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.storehouse.entity.StorehouseSpuClassBind;
import cn.atsoft.dasheng.storehouse.model.params.StorehouseSpuClassBindParam;
import cn.atsoft.dasheng.storehouse.model.result.StorehouseSpuClassBindResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 仓库物料分类绑定表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-15
 */
public interface StorehouseSpuClassBindService extends IService<StorehouseSpuClassBind> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2023-05-15
     */
    void add(StorehouseSpuClassBindParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2023-05-15
     */
    void delete(StorehouseSpuClassBindParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2023-05-15
     */
    void update(StorehouseSpuClassBindParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-15
     */
    StorehouseSpuClassBindResult findBySpec(StorehouseSpuClassBindParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-15
     */
    List<StorehouseSpuClassBindResult> findListBySpec(StorehouseSpuClassBindParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-15
     */
     PageInfo<StorehouseSpuClassBindResult> findPageBySpec(StorehouseSpuClassBindParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-15
     */
     PageInfo<StorehouseSpuClassBindResult> findPageBySpec(StorehouseSpuClassBindParam param,DataScope dataScope);

}
