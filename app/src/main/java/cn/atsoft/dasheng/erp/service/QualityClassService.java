package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityClass;
import cn.atsoft.dasheng.erp.model.params.QualityClassParam;
import cn.atsoft.dasheng.erp.model.result.QualityClassResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 质检方案详情分类 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-28
 */
public interface QualityClassService extends IService<QualityClass> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-28
     */
    Long add(QualityClassParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-28
     */
    void delete(QualityClassParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-28
     */
    void update(QualityClassParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-28
     */
    QualityClassResult findBySpec(QualityClassParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-28
     */
    List<QualityClassResult> findListBySpec(QualityClassParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-28
     */
     PageInfo<QualityClassResult> findPageBySpec(QualityClassParam param);

}
