package cn.atsoft.dasheng.purchase.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.purchase.entity.ProcurementOrder;
import cn.atsoft.dasheng.purchase.model.params.ProcurementOrderParam;
import cn.atsoft.dasheng.purchase.model.result.ProcurementOrderResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 采购单 服务类
 * </p>
 *
 * @author song
 * @since 2022-01-13
 */
public interface ProcurementOrderService extends IService<ProcurementOrder> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-01-13
     */
    void add(ProcurementOrderParam param) throws Exception;

    /**
     * 删除
     *
     * @author song
     * @Date 2022-01-13
     */
    void delete(ProcurementOrderParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-01-13
     */
    void update(ProcurementOrderParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-01-13
     */
    ProcurementOrderResult findBySpec(ProcurementOrderParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-01-13
     */
    List<ProcurementOrderResult> findListBySpec(ProcurementOrderParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-01-13
     */
     PageInfo<ProcurementOrderResult> findPageBySpec(ProcurementOrderParam param);

    void updateStatus(ActivitiProcessTask processTask);

    void updateRefuseStatus(ActivitiProcessTask processTask);
}
