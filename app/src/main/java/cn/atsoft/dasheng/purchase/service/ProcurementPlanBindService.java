package cn.atsoft.dasheng.purchase.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlanBind;
import cn.atsoft.dasheng.purchase.model.params.ProcurementPlanBindParam;
import cn.atsoft.dasheng.purchase.model.params.ProcurementPlanParam;
import cn.atsoft.dasheng.purchase.model.result.ProcurementPlanBindResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author song
 * @since 2021-12-21
 */
public interface ProcurementPlanBindService extends IService<ProcurementPlanBind> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-12-21
     */
    void add(ProcurementPlanBindParam param);

    void batchAdd(ProcurementPlanParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-12-21
     */
    void delete(ProcurementPlanBindParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-12-21
     */
    void update(ProcurementPlanBindParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-12-21
     */
    ProcurementPlanBindResult findBySpec(ProcurementPlanBindParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-12-21
     */
    List<ProcurementPlanBindResult> findListBySpec(ProcurementPlanBindParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-12-21
     */
    PageInfo<ProcurementPlanBindResult> findPageBySpec(ProcurementPlanBindParam param);

    /**
     *
      * @param planIds
     * @return
     */
    List<ProcurementPlanBindResult> getDetail(List<Long> planIds);

}
