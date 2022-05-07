package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.app.entity.Stock;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Supply;
import cn.atsoft.dasheng.erp.entity.Inkind;
import cn.atsoft.dasheng.erp.entity.InstockOrder;
import cn.atsoft.dasheng.erp.entity.StorehousePositionsBind;
import cn.atsoft.dasheng.erp.model.params.InstockOrderParam;
import cn.atsoft.dasheng.erp.model.request.InstockParams;
import cn.atsoft.dasheng.erp.model.result.InstockOrderResult;
import cn.atsoft.dasheng.erp.pojo.FreeInStockParam;
import cn.atsoft.dasheng.erp.pojo.InStockByOrderParam;
import cn.atsoft.dasheng.erp.pojo.InstockListRequest;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.purchase.pojo.ListingPlan;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 入库单 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-06
 */
public interface InstockOrderService extends IService<InstockOrder> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-06
     */
    void add(InstockOrderParam param);

    void checkNumberTrue(Long id, Integer status, Long actionId);

    void checkNumberFalse(Long id, Integer status);

    void checkAction(Long id, Long actionId);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-06
     */
    void delete(InstockOrderParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-06
     */
    void update(InstockOrderParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-06
     */
    InstockOrderResult findBySpec(InstockOrderParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-06
     */
    List<InstockOrderResult> findListBySpec(InstockOrderParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-06
     */
    PageInfo<InstockOrderResult> findPageBySpec(InstockOrderParam param);


    /**
     * 通过质检创建入库单
     *
     * @param instockParams
     */
    void addByQuality(InstockParams instockParams);

    /**
     * 自由入库
     *
     * @param freeInStockParam
     */
    void freeInstock(FreeInStockParam freeInStockParam);

    void formatDetail(InstockOrderResult orderResult);


    void formatResult(InstockOrderResult result);

    boolean inStockByOrder(InStockByOrderParam param);

    /**
     * 多个库位入库
     *
     * @param stockParam
     */
    void freeInStockByPositions(FreeInStockParam stockParam);


    Stock judgeStockExist(Inkind inkind, List<Stock> stocks);

    boolean judgeSkuBind(Inkind inkind, List<Supply> supplies);

    Boolean judgePosition(List<StorehousePositionsBind> positionsBinds, Inkind inkind);

    void updateStatus(ActivitiProcessTask processTask);

    void updateCreateInstockStatus(ActivitiProcessTask processTask);

    void updateRefuseStatus(ActivitiProcessTask processTask);


    void updateCreateInstockRefuseStatus(ActivitiProcessTask processTask);
}
