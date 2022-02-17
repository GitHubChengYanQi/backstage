package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.SopBind;
import cn.atsoft.dasheng.production.model.params.SopBindParam;
import cn.atsoft.dasheng.production.model.result.ShipSetpResult;
import cn.atsoft.dasheng.production.model.result.SopBindResult;
import cn.atsoft.dasheng.production.model.result.SopResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * sop 绑定 工序 服务类
 * </p>
 *
 * @author song
 * @since 2022-02-17
 */
public interface SopBindService extends IService<SopBind> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-02-17
     */
    void add(SopBindParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-02-17
     */
    void delete(SopBindParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-02-17
     */
    void update(SopBindParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-02-17
     */
    SopBindResult findBySpec(SopBindParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-02-17
     */
    List<SopBindResult> findListBySpec(SopBindParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-02-17
     */
     PageInfo<SopBindResult> findPageBySpec(SopBindParam param);

    Map<Long, SopResult> getSop(List<Long> shipId);


    Map<Long, List<ShipSetpResult>> getShipSetp(List<Long> sopId);
}
