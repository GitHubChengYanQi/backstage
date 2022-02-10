package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ShipSetpBind;
import cn.atsoft.dasheng.production.model.params.ShipSetpBindParam;
import cn.atsoft.dasheng.production.model.result.ShipSetpBindResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 工序关联绑定工具与设备表 服务类
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
public interface ShipSetpBindService extends IService<ShipSetpBind> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-02-10
     */
    void add(ShipSetpBindParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-02-10
     */
    void delete(ShipSetpBindParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-02-10
     */
    void update(ShipSetpBindParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-02-10
     */
    ShipSetpBindResult findBySpec(ShipSetpBindParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-02-10
     */
    List<ShipSetpBindResult> findListBySpec(ShipSetpBindParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-02-10
     */
     PageInfo<ShipSetpBindResult> findPageBySpec(ShipSetpBindParam param);

}
