package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.WorkingProcedure;
import cn.atsoft.dasheng.production.model.params.WorkingProcedureParam;
import cn.atsoft.dasheng.production.model.result.WorkingProcedureResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 工序表 服务类
 * </p>
 *
 * @author 
 * @since 2021-10-29
 */
public interface WorkingProcedureService extends IService<WorkingProcedure> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-10-29
     */
    void add(WorkingProcedureParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-10-29
     */
    void delete(WorkingProcedureParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-10-29
     */
    void update(WorkingProcedureParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-10-29
     */
    WorkingProcedureResult findBySpec(WorkingProcedureParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-10-29
     */
    List<WorkingProcedureResult> findListBySpec(WorkingProcedureParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-10-29
     */
     PageInfo<WorkingProcedureResult> findPageBySpec(WorkingProcedureParam param);

}
