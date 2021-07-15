package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Outbound;
import cn.atsoft.dasheng.app.model.params.OutboundParam;
import cn.atsoft.dasheng.app.model.result.OutboundResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 出库表 服务类
 * </p>
 *
 * @author 
 * @since 2021-07-15
 */
public interface OutboundService extends IService<Outbound> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-07-15
     */
    void add(OutboundParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-07-15
     */
    void delete(OutboundParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-07-15
     */
    void update(OutboundParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-07-15
     */
    OutboundResult findBySpec(OutboundParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-07-15
     */
    List<OutboundResult> findListBySpec(OutboundParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-07-15
     */
     PageInfo<OutboundResult> findPageBySpec(OutboundParam param);

}
