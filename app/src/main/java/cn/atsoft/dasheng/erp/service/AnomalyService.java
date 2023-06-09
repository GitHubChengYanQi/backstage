package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Anomaly;
import cn.atsoft.dasheng.erp.model.params.AnomalyDetailParam;
import cn.atsoft.dasheng.erp.model.params.AnomalyParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyResult;
import cn.atsoft.dasheng.erp.pojo.AnomalyCensus;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author song
 * @since 2022-05-27
 */
public interface AnomalyService extends IService<Anomaly> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-05-27
     */
    Anomaly add(AnomalyParam param);

    List<AnomalyCensus> anomalyCensus(AnomalyParam param);

    Map<Integer, List<AnomalyResult>> detailed(AnomalyParam param);

    AnomalyResult detail(Long id);

    Long temporary(AnomalyParam param);

    Map<Long, AnomalyResult> getMap(List<Long> ids);

    void addInventoryRecord(AnomalyParam param,Long inventoryId, int status);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-05-27
     */
    void delete(AnomalyParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-05-27
     */
    Anomaly update(AnomalyParam param);

    void deleteBind(Long anomalyId);

    void dealWithError(AnomalyParam param);

    List<Long> createInkind(AnomalyParam param, AnomalyDetailParam detailParam);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-05-27
     */
    AnomalyResult findBySpec(AnomalyParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-05-27
     */
    List<AnomalyResult> findListBySpec(AnomalyParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-05-27
     */
     PageInfo<AnomalyResult> findPageBySpec(AnomalyParam param);

    List<AnomalyResult> anomalyIsComplete(List<Long> skuIds, List<Long> brandIds, List<Long> positionIds);

    void getOrder(List<AnomalyResult> data);

    void detailFormat(AnomalyResult result);

    void format(List<AnomalyResult> data);
}
