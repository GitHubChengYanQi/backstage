package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityTaskBind;
import cn.atsoft.dasheng.erp.model.params.QualityTaskBindParam;
import cn.atsoft.dasheng.erp.model.result.QualityTaskBindResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author song
 * @since 2021-11-17
 */
public interface QualityTaskBindService extends IService<QualityTaskBind> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-11-17
     */
    void add(QualityTaskBindParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-11-17
     */
    void delete(QualityTaskBindParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-11-17
     */
    void update(QualityTaskBindParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-11-17
     */
    QualityTaskBindResult findBySpec(QualityTaskBindParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-11-17
     */
    List<QualityTaskBindResult> findListBySpec(QualityTaskBindParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-11-17
     */
     PageInfo<QualityTaskBindResult> findPageBySpec(QualityTaskBindParam param);

}
