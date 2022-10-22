package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Anomaly;
import cn.atsoft.dasheng.erp.entity.AnomalyDetail;
import cn.atsoft.dasheng.erp.model.params.AnomalyDetailParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyDetailResult;
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
public interface AnomalyDetailService extends IService<AnomalyDetail> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-05-27
     */
    void add(AnomalyDetailParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-05-27
     */
    void delete(AnomalyDetailParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-05-27
     */
    void update(AnomalyDetailParam param);

    void allowEdit(Anomaly anomaly);

    void pushPeople(Long userId, Long id);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-05-27
     */
    AnomalyDetailResult findBySpec(AnomalyDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-05-27
     */
    List<AnomalyDetailResult> findListBySpec(AnomalyDetailParam param);

    List<AnomalyDetailResult> getDetails(Long anomalyId);


    Map<Long, Long> inStockErrorNum(List<Long> orderIds);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-05-27
     */
     PageInfo<AnomalyDetailResult> findPageBySpec(AnomalyDetailParam param);

    void format(List<AnomalyDetailResult> data);
}
