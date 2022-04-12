package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AnomalyDetail;
import cn.atsoft.dasheng.erp.model.params.AnomalyDetailParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 异常详情 服务类
 * </p>
 *
 * @author song
 * @since 2022-04-12
 */
public interface AnomalyDetailService extends IService<AnomalyDetail> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-04-12
     */
    void add(AnomalyDetailParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-04-12
     */
    void delete(AnomalyDetailParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-04-12
     */
    void update(AnomalyDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-04-12
     */
    AnomalyDetailResult findBySpec(AnomalyDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-04-12
     */
    List<AnomalyDetailResult> findListBySpec(AnomalyDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-04-12
     */
     PageInfo<AnomalyDetailResult> findPageBySpec(AnomalyDetailParam param);

}
