package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.DaoxinShipSetpClass;
import cn.atsoft.dasheng.production.model.params.DaoxinShipSetpClassParam;
import cn.atsoft.dasheng.production.model.result.DaoxinShipSetpClassResult;
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
public interface DaoxinShipSetpClassService extends IService<DaoxinShipSetpClass> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-02-10
     */
    void add(DaoxinShipSetpClassParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-02-10
     */
    void delete(DaoxinShipSetpClassParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-02-10
     */
    void update(DaoxinShipSetpClassParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-02-10
     */
    DaoxinShipSetpClassResult findBySpec(DaoxinShipSetpClassParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-02-10
     */
    List<DaoxinShipSetpClassResult> findListBySpec(DaoxinShipSetpClassParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-02-10
     */
     PageInfo<DaoxinShipSetpClassResult> findPageBySpec(DaoxinShipSetpClassParam param);

}
