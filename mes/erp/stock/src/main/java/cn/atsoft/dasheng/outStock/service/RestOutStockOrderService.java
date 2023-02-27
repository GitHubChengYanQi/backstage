package cn.atsoft.dasheng.outStock.service;

//import cn.atsoft.dasheng.app.model.request.StockView;
//import cn.atsoft.dasheng.app.model.result.StorehouseResult;
//import cn.atsoft.dasheng.app.pojo.StockSkuBrand;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
//import cn.atsoft.dasheng.erp.model.params.DataStatisticsViewParam;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.outStock.entity.RestOutStockOrder;
//import cn.atsoft.dasheng.production.entity.RestOutStockOrder;
//import cn.atsoft.dasheng.production.model.params.RestOutStockOrderDetailParam;
//import cn.atsoft.dasheng.production.model.params.RestOutStockOrderParam;
//import cn.atsoft.dasheng.production.model.result.RestOutStockOrderCartResult;
//import cn.atsoft.dasheng.production.model.result.RestOutStockOrderResult;
import cn.atsoft.dasheng.outStock.model.params.RestOutStockOrderParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 领料单 服务类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-25
 */
public interface RestOutStockOrderService extends IService<RestOutStockOrder> {

//    /**
//     * 新增
//     *
//     * @author Captain_Jazz
//     * @Date 2022-03-25
//     */
    RestOutStockOrder add(RestOutStockOrderParam param);

//    /**
//     * 删除
//     *
//     * @author Captain_Jazz
//     * @Date 2022-03-25
//     */
//    void delete(RestOutStockOrderParam param);
//
//    /**
//     * 更新
//     *
//     * @author Captain_Jazz
//     * @Date 2022-03-25
//     */
//    void update(RestOutStockOrderParam param);
//
//    /**
//     * 查询单条数据，Specification模式
//     *
//     * @author Captain_Jazz
//     * @Date 2022-03-25
//     */
//    RestOutStockOrderResult findBySpec(RestOutStockOrderParam param);
//
    String createCode(RestOutStockOrderParam param);
//
//    /**
//     * 查询列表，Specification模式
//     *
//     * @author Captain_Jazz
//     * @Date 2022-03-25
//     */
//    List<RestOutStockOrderResult> findListBySpec(RestOutStockOrderParam param);
//
//    /**
//     * 查询分页数据，Specification模式
//     *
//     * @author Captain_Jazz
//     * @Date 2022-03-25
//     */
//    PageInfo findPageBySpec(RestOutStockOrderParam param);
//
//    void taskFormat(List<RestOutStockOrderResult> results);
//
//    void format(List<RestOutStockOrderResult> results);
//
//
//    String addByProductionTask(Object param);
//
//    void sendPersonPick(RestOutStockOrderParam param);
//
//    void warning(RestOutStockOrderParam param);
//
////    boolean updateStock(RestOutStockOrderDetail detailParam, List<StockSkuBrand> stockSkuBrands);
//
//    List<RestStorehouseResult> getStockSkus(List<Long> skuIds);
//
//
//    Map<Integer, List<ActivitiProcessTask>> unExecuted(Long taskId);
//
//
    String outStock(RestOutStockOrderParam param);
//
//
//    void outStockBySku(RestOutStockOrderParam param);
//
//    RestOutStockOrderResult detail(Long id);
//
//    void updateStatus(ActivitiProcessTask processTask);
//
//    List<Map<String,Object>> listByUser(RestOutStockOrderParam pickListsParam);
//
//    void abortCode(String code);
//
//    List<RestOutStockCart>  listByCode(String code);
//
//    List<Long> idsList(RestOutStockOrderParam param);
//
//    void updateOutStockRefuseStatus(ActivitiProcessTask processTask);
//
//    Page<StockView> outStockUserView(DataStatisticsViewParam param);
//
//   List<StockView> outStockView(DataStatisticsViewParam param);
}
