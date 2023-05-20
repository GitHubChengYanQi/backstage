package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.StorehousePositionsDeptBind;
import cn.atsoft.dasheng.erp.model.params.StorehousePositionsDeptBindParam;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsDeptBindResult;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * <p>
 * 库位权限绑定表 服务类
 * </p>
 *
 * @author 
 * @since 2022-01-25
 */
public interface StorehousePositionsDeptBindService extends IService<StorehousePositionsDeptBind> {

    /**
     * 新增
     *
     * @author 
     * @Date 2022-01-25
     */
    void add(StorehousePositionsDeptBindParam param);

    @Async
    void addBatch(List<StorehousePositionsDeptBindParam> params);

    /**
     * 删除
     *
     * @author 
     * @Date 2022-01-25
     */
    void delete(StorehousePositionsDeptBindParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2022-01-25
     */
    void update(StorehousePositionsDeptBindParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2022-01-25
     */
    StorehousePositionsDeptBindResult findBySpec(StorehousePositionsDeptBindParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2022-01-25
     */
    List<StorehousePositionsDeptBindResult> findListBySpec(StorehousePositionsDeptBindParam param);

    List<StorehousePositionsDeptBindResult> getBindByPositionIds(List<Long> positionIds);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2022-01-25
     */
     PageInfo<StorehousePositionsDeptBindResult> findPageBySpec(StorehousePositionsDeptBindParam param);

}
