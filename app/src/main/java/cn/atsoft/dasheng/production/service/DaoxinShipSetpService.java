package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.DaoxinShipSetp;
import cn.atsoft.dasheng.production.model.params.DaoxinShipSetpParam;
import cn.atsoft.dasheng.production.model.result.DaoxinShipSetpResult;
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
public interface DaoxinShipSetpService extends IService<DaoxinShipSetp> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-02-10
     */
    void add(DaoxinShipSetpParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-02-10
     */
    void delete(DaoxinShipSetpParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-02-10
     */
    void update(DaoxinShipSetpParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-02-10
     */
    DaoxinShipSetpResult findBySpec(DaoxinShipSetpParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-02-10
     */
    List<DaoxinShipSetpResult> findListBySpec(DaoxinShipSetpParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-02-10
     */
     PageInfo<DaoxinShipSetpResult> findPageBySpec(DaoxinShipSetpParam param);

}
