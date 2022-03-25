package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.production.entity.ShipSetp;
import cn.atsoft.dasheng.production.model.params.ShipSetpParam;
import cn.atsoft.dasheng.production.model.result.ShipSetpResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 工序表 服务类
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
public interface ShipSetpService extends IService<ShipSetp> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-02-10
     */
    void add(ShipSetpParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-02-10
     */
    void delete(ShipSetpParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-02-10
     */
    void update(ShipSetpParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-02-10
     */
    ShipSetpResult findBySpec(ShipSetpParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-02-10
     */
    List<ShipSetpResult> findListBySpec(ShipSetpParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-02-10
     */
     PageInfo<ShipSetpResult> findPageBySpec(ShipSetpParam param, DataScope dataScope);

    void format(List<ShipSetpResult> param);

    List<ShipSetpResult> getResultsByids(List<Long> ids);
}
