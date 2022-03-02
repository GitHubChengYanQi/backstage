package cn.atsoft.dasheng.purchase.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.purchase.entity.PurchaseAsk;
import cn.atsoft.dasheng.purchase.model.params.PurchaseAskParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseAskResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 采购申请 服务类
 * </p>
 *
 * @author song
 * @since 2021-12-15
 */
public interface PurchaseAskService extends IService<PurchaseAsk> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-12-15
     */
    void add(PurchaseAskParam param);
//
//    /**
//     * 删除
//     *
//     * @author song
//     * @Date 2021-12-15
//     */
//    void delete(PurchaseAskParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-12-15
     */
    void update(PurchaseAskParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-12-15
     */
    PurchaseAskResult findBySpec(PurchaseAskParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-12-15
     */
    List<PurchaseAskResult> findListBySpec(PurchaseAskParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-12-15
     */
    PageInfo<PurchaseAskResult> findPageBySpec(PurchaseAskParam param);

    PurchaseAskResult detail(PurchaseAskParam param);

    /**
     * @param taskId
     */
    void updateStatus(ActivitiProcessTask processTask);

    void updateRefuseStatus(ActivitiProcessTask param);

    void rejected(Long askId);

    void complateAsk(Long processTaskId);

    List<PurchaseAskResult> getResults(List<Long> askIds);

    List<PurchaseAskResult> listResultByIds(List<Long> ids);
}
