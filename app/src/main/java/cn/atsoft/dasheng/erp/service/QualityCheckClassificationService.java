package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityCheckClassification;
import cn.atsoft.dasheng.erp.model.params.QualityCheckClassificationParam;
import cn.atsoft.dasheng.erp.model.result.QualityCheckClassificationResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 质检分类表 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-27
 */
public interface QualityCheckClassificationService extends IService<QualityCheckClassification> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-27
     */
    void add(QualityCheckClassificationParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-27
     */
    void delete(QualityCheckClassificationParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-27
     */
    void update(QualityCheckClassificationParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-27
     */
    QualityCheckClassificationResult findBySpec(QualityCheckClassificationParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-27
     */
    List<QualityCheckClassificationResult> findListBySpec(QualityCheckClassificationParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-27
     */
     PageInfo<QualityCheckClassificationResult> findPageBySpec(QualityCheckClassificationParam param);

}
