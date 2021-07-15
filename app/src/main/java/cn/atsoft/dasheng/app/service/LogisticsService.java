package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Logistics;
import cn.atsoft.dasheng.app.model.params.LogisticsParam;
import cn.atsoft.dasheng.app.model.result.LogisticsResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 物流表 服务类
 * </p>
 *
 * @author 
 * @since 2021-07-15
 */
public interface LogisticsService extends IService<Logistics> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-07-15
     */
    void add(LogisticsParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-07-15
     */
    void delete(LogisticsParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-07-15
     */
    void update(LogisticsParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-07-15
     */
    LogisticsResult findBySpec(LogisticsParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-07-15
     */
    List<LogisticsResult> findListBySpec(LogisticsParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-07-15
     */
     PageInfo<LogisticsResult> findPageBySpec(LogisticsParam param);

}
