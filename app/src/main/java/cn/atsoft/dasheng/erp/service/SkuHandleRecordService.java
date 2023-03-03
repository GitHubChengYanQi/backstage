package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.SkuHandleRecord;
import cn.atsoft.dasheng.erp.model.params.SkuHandleRecordParam;
import cn.atsoft.dasheng.erp.model.result.SkuHandleRecordResult;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * sku 任务操作记录 服务类
 * </p>
 *
 * @author 
 * @since 2022-10-25
 */
public interface SkuHandleRecordService extends IService<SkuHandleRecord> {

    /**
     * 新增
     *
     * @author 
     * @Date 2022-10-25
     */
    void add(SkuHandleRecordParam param);

    void addRecord(Long skuId, Long brandId, Long positionId, Long customerId, String source, ActivitiProcessTask task, Long operationNumber, Long nowStockNum, Long balanceNumber);

    void addRecord(Long skuId, Long brandId, Long positionId, Long customerId, Long taskId, Long number, String source);

    void addOutRecord(Long skuId, Long brandId, Long positionId, Long customerId, Long taskId, Long number, String source);

    /**
     * 删除
     *
     * @author 
     * @Date 2022-10-25
     */
    void delete(SkuHandleRecordParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2022-10-25
     */
    void update(SkuHandleRecordParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2022-10-25
     */
    SkuHandleRecordResult findBySpec(SkuHandleRecordParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2022-10-25
     */
    List<SkuHandleRecordResult> findListBySpec(SkuHandleRecordParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2022-10-25
     */
     PageInfo<SkuHandleRecordResult> findPageBySpec(SkuHandleRecordParam param);

}
