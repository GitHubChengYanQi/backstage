package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ShipSetpClass;
import cn.atsoft.dasheng.production.model.params.ShipSetpClassParam;
import cn.atsoft.dasheng.production.model.result.ShipSetpClassResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 工序分类表 服务类
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
public interface ShipSetpClassService extends IService<ShipSetpClass> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-02-10
     */
    void add(ShipSetpClassParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-02-10
     */
    void delete(ShipSetpClassParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-02-10
     */
    void update(ShipSetpClassParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-02-10
     */
    ShipSetpClassResult findBySpec(ShipSetpClassParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-02-10
     */
    List<ShipSetpClassResult> findListBySpec(ShipSetpClassParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-02-10
     */
     PageInfo<ShipSetpClassResult> findPageBySpec(ShipSetpClassParam param);

}
