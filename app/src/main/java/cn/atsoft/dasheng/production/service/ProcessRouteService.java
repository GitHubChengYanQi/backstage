package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProcessRoute;
import cn.atsoft.dasheng.production.model.params.ProcessRouteParam;
import cn.atsoft.dasheng.production.model.result.ProcessRouteResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 工艺路线列表 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-02-15
 */
public interface ProcessRouteService extends IService<ProcessRoute> {

    /**
     * 新增
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    void add(ProcessRouteParam param);

    /**
     * 删除
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    void delete(ProcessRouteParam param);

    /**
     * 更新
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    void update(ProcessRouteParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    ProcessRouteResult findBySpec(ProcessRouteParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    List<ProcessRouteResult> findListBySpec(ProcessRouteParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
     PageInfo<ProcessRouteResult> findPageBySpec(ProcessRouteParam param);

    ProcessRoute getEntity(ProcessRouteParam param);

    ProcessRouteResult detail(Long id);

    ProcessRouteResult getRouteBySkuId(Long skuId);
}
