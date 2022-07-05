package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AnomalyOrder;
import cn.atsoft.dasheng.erp.model.params.AnomalyOrderParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyOrderResult;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author song
 * @since 2022-06-09
 */
public interface AnomalyOrderService extends IService<AnomalyOrder> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-06-09
     */
    void add(AnomalyOrderParam param);


    void addByInventory(AnomalyOrderParam param);

    void submit(AnomalyOrderParam orderParam);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-06-09
     */
    void delete(AnomalyOrderParam param);

    AnomalyOrderResult detail(Long id);

    void updateStatus(ActivitiProcessTask processTask);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-06-09
     */
    void update(AnomalyOrderParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-06-09
     */
    AnomalyOrderResult findBySpec(AnomalyOrderParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-06-09
     */
    List<AnomalyOrderResult> findListBySpec(AnomalyOrderParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-06-09
     */
     PageInfo<AnomalyOrderResult> findPageBySpec(AnomalyOrderParam param);

}
