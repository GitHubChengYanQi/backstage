package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityTaskRefuse;
import cn.atsoft.dasheng.erp.model.params.QualityTaskRefuseParam;
import cn.atsoft.dasheng.erp.model.result.QualityTaskRefuseResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 质检任务拒检 服务类
 * </p>
 *
 * @author song
 * @since 2021-12-14
 */
public interface QualityTaskRefuseService extends IService<QualityTaskRefuse> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-12-14
     */
    void add(QualityTaskRefuseParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-12-14
     */
    void delete(QualityTaskRefuseParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-12-14
     */
    void update(QualityTaskRefuseParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-12-14
     */
    QualityTaskRefuseResult findBySpec(QualityTaskRefuseParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-12-14
     */
    List<QualityTaskRefuseResult> findListBySpec(QualityTaskRefuseParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-12-14
     */
    PageInfo<QualityTaskRefuseResult> findPageBySpec(QualityTaskRefuseParam param);

    /**
     * 主任务拒检
     *
     * @param param
     */
    void refuse(QualityTaskRefuseParam param);

    List<QualityTaskRefuseResult> getRefuseByDetailId(Long detailId);

    List<QualityTaskRefuseResult> getRefuseByTaskId(Long taskId);

}
