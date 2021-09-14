package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Plan;
import cn.atsoft.dasheng.crm.model.params.PlanParam;
import cn.atsoft.dasheng.crm.model.result.PlanResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author song
 * @since 2021-09-14
 */
public interface PlanService extends IService<Plan> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-09-14
     */
    void add(PlanParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-09-14
     */
    void delete(PlanParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-09-14
     */
    void update(PlanParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-09-14
     */
    PlanResult findBySpec(PlanParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-09-14
     */
    List<PlanResult> findListBySpec(PlanParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-09-14
     */
     PageInfo<PlanResult> findPageBySpec(PlanParam param);

}
