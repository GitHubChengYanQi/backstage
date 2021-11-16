package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityIssuess;
import cn.atsoft.dasheng.erp.model.params.QualityIssuessParam;
import cn.atsoft.dasheng.erp.model.result.QualityIssuessResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 质检事项表 服务类
 * </p>
 *
 * @author 
 * @since 2021-11-15
 */
public interface QualityIssuessService extends IService<QualityIssuess> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-11-15
     */
    void add(QualityIssuessParam param);
    /**
     * 详情格式化
     *
     * @author
     * @Date 2021-11-15
     */
    void detailFormat(QualityIssuessResult param);
    /**
     * 删除
     *
     * @author 
     * @Date 2021-11-15
     */
    void delete(QualityIssuessParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-11-15
     */
    void update(QualityIssuessParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-11-15
     */
    QualityIssuessResult findBySpec(QualityIssuessParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-11-15
     */
    List<QualityIssuessResult> findListBySpec(QualityIssuessParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-11-15
     */
     PageInfo<QualityIssuessResult> findPageBySpec(QualityIssuessParam param);

}
