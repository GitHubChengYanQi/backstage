package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityCheck;
import cn.atsoft.dasheng.erp.model.params.QualityCheckParam;
import cn.atsoft.dasheng.erp.model.result.QualityCheckResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 质检表 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-27
 */
public interface QualityCheckService extends IService<QualityCheck> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-27
     */
    void add(QualityCheckParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-27
     */
    void delete(QualityCheckParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-27
     */
    void update(QualityCheckParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-27
     */
    QualityCheckResult findBySpec(QualityCheckParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-27
     */
    List<QualityCheckResult> findListBySpec(QualityCheckParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-27
     */
    PageInfo<QualityCheckResult> findPageBySpec(QualityCheckParam param);

    List<QualityCheckResult> getChecks(List<Long> ids);

}
