package cn.atsoft.dasheng.purchase.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.ProcurementOrderDetail;
import cn.atsoft.dasheng.purchase.model.params.ProcurementOrderDetailParam;
import cn.atsoft.dasheng.purchase.model.result.ProcurementOrderDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author song
 * @since 2022-01-13
 */
public interface ProcurementOrderDetailService extends IService<ProcurementOrderDetail> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-01-13
     */
    void add(ProcurementOrderDetailParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-01-13
     */
    void delete(ProcurementOrderDetailParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-01-13
     */
    void update(ProcurementOrderDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-01-13
     */
    ProcurementOrderDetailResult findBySpec(ProcurementOrderDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-01-13
     */
    List<ProcurementOrderDetailResult> findListBySpec(ProcurementOrderDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-01-13
     */
     PageInfo<ProcurementOrderDetailResult> findPageBySpec(ProcurementOrderDetailParam param);

}
