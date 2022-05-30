package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Anomaly;
import cn.atsoft.dasheng.erp.model.params.AnomalyParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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
    void add(AnomalyParam param);

    AnomalyResult detail(Long id);

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
    void update(AnomalyParam param);

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

}
