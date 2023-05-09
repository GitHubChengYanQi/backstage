package cn.atsoft.dasheng.sys.modular.system.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.sys.modular.system.entity.PositionBind;
import cn.atsoft.dasheng.sys.modular.system.model.params.PositionBindParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.PositionBindResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 租户用户位置绑定表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-09
 */
public interface PositionBindService extends IService<PositionBind> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    void add(PositionBindParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    void delete(PositionBindParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    void update(PositionBindParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    PositionBindResult findBySpec(PositionBindParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    List<PositionBindResult> findListBySpec(PositionBindParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
     PageInfo<PositionBindResult> findPageBySpec(PositionBindParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
     PageInfo<PositionBindResult> findPageBySpec(PositionBindParam param,DataScope dataScope);

}
