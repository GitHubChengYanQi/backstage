package cn.atsoft.dasheng.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
//import cn.atsoft.dasheng.erp.entity.Traceability;
//import cn.atsoft.dasheng.erp.model.params.TraceabilityParam;
//import cn.atsoft.dasheng.erp.model.result.TraceabilityResult;
import cn.atsoft.dasheng.entity.RestTraceability;
import cn.atsoft.dasheng.model.params.RestTraceabilityParam;
import cn.atsoft.dasheng.model.result.RestTraceabilityResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 实物表 服务类
 * </p>
 *
 * @author song
 * @since 2021-11-01
 */
public interface RestTraceabilityService extends IService<RestTraceability> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-11-01
     */
    Long add(RestTraceabilityParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-11-01
     */
    void delete(RestTraceabilityParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-11-01
     */
    void update(RestTraceabilityParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-11-01
     */
    RestTraceabilityResult findBySpec(RestTraceabilityParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-11-01
     */
    List<RestTraceabilityResult> findListBySpec(RestTraceabilityParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-11-01
     */
    PageInfo<RestTraceabilityResult> findPageBySpec(RestTraceabilityParam param);


    PageInfo<RestTraceabilityResult> stockTraceability(RestTraceabilityParam param);

    void updateAnomalyTraceability(List<Long> TraceabilityIds);

    RestTraceabilityResult backTraceabilitygetById(Long id);


    List<Long> anomalySku(List<Long> TraceabilityIds);

    List<RestTraceabilityResult> getTraceabilitys(List<Long> TraceabilityIds);


//    List<TraceabilityResult> details(TraceabilityParam param);

//    TraceabilityResult TraceabilityDetail(TraceabilityParam param);

//    TraceabilityResult getTraceabilityResult(Long id);

//    void resultFormat(List<TraceabilityResult> data);
}
