package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityTaskDetail;
import cn.atsoft.dasheng.erp.model.params.QualityTaskDetailParam;
import cn.atsoft.dasheng.erp.model.result.QualityTaskDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 质检任务详情 服务类
 * </p>
 *
 * @author 
 * @since 2021-11-16
 */
public interface QualityTaskDetailService extends IService<QualityTaskDetail> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-11-16
     */
    void add(QualityTaskDetailParam param);

    void format(List<QualityTaskDetailResult> param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-11-16
     */
    void delete(QualityTaskDetailParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-11-16
     */
    void update(QualityTaskDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-11-16
     */
    QualityTaskDetailResult findBySpec(QualityTaskDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-11-16
     */
    List<QualityTaskDetailResult> findListBySpec(QualityTaskDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-11-16
     */
     PageInfo<QualityTaskDetailResult> findPageBySpec(QualityTaskDetailParam param);

}