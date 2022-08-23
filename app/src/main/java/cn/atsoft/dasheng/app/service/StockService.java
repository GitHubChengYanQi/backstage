package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.app.pojo.AddStockParam;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Stock;
import cn.atsoft.dasheng.app.model.params.StockParam;
import cn.atsoft.dasheng.app.model.result.StockResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.erp.entity.Inkind;
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
     * @return
     * @author
     * @Date 2021-07-15
     */
    Long update(StockParam param);

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

    void updateNumber(List<Long> stockId);

    //添加库存
    void addDetails(AddStockParam stockParam);

    /**
     * 查询分页数据，Specification模式
     *
     * @author
     * @Date 2021-07-15
     */
    PageInfo findPageBySpec(StockParam param, DataScope dataScope);

    void batchDelete(List<Long> Ids);

    List<Stock> getStockByInKind(List<Inkind> inkinds, List<Long> houseId);
}
