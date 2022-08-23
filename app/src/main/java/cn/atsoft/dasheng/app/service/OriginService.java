package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Origin;
import cn.atsoft.dasheng.app.model.params.OriginParam;
import cn.atsoft.dasheng.app.model.result.OriginResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 来源表 服务类
 * </p>
 *
 * @author 
 * @since 2021-07-19
 */
public interface OriginService extends IService<Origin> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-07-19
     */
    Long add(OriginParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-07-19
     */
    void delete(OriginParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-07-19
     */
    void update(OriginParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-07-19
     */
    OriginResult findBySpec(OriginParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-07-19
     */
    List<OriginResult> findListBySpec(OriginParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-07-19
     */
     PageInfo findPageBySpec(OriginParam param, DataScope dataScope );

     void batchDelete (List<Long> ids);

}
