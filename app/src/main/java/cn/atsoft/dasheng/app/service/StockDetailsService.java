package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 仓库物品明细表 服务类
 * </p>
 *
 * @author 
 * @since 2021-07-15
 */
public interface StockDetailsService extends IService<StockDetails> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-07-15
     */
    void add(StockDetailsParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-07-15
     */
    void delete(StockDetailsParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-07-15
     */
    void update(StockDetailsParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-07-15
     */
    StockDetailsResult findBySpec(StockDetailsParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-07-15
     */
    List<StockDetailsResult> findListBySpec(StockDetailsParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-07-15
     */
     PageInfo<StockDetailsResult> findPageBySpec(StockDetailsParam param);

}