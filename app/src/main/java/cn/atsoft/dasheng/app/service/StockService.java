package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Stock;
import cn.atsoft.dasheng.app.model.params.StockParam;
import cn.atsoft.dasheng.app.model.result.StockResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 仓库总表 服务类
 * </p>
 *
 * @author 
 * @since 2021-07-15
 */
public interface StockService extends IService<Stock> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-07-15
     */
    Long add(StockParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-07-15
     */
    void delete(StockParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-07-15
     */
    void update(StockParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-07-15
     */
    StockResult findBySpec(StockParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-07-15
     */
    List<StockResult> findListBySpec(StockParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-07-15
     */
     PageInfo<StockResult> findPageBySpec(StockParam param);

}
