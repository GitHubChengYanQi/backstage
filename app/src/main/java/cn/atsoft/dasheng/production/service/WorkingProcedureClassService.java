package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.WorkingProcedureClass;
import cn.atsoft.dasheng.production.model.params.WorkingProcedureClassParam;
import cn.atsoft.dasheng.production.model.result.WorkingProcedureClassResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 工序分类表 服务类
 * </p>
 *
 * @author 
 * @since 2021-10-29
 */
public interface WorkingProcedureClassService extends IService<WorkingProcedureClass> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-10-29
     */
    void add(WorkingProcedureClassParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-10-29
     */
    void delete(WorkingProcedureClassParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-10-29
     */
    void update(WorkingProcedureClassParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-10-29
     */
    WorkingProcedureClassResult findBySpec(WorkingProcedureClassParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-10-29
     */
    List<WorkingProcedureClassResult> findListBySpec(WorkingProcedureClassParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-10-29
     */
     PageInfo<WorkingProcedureClassResult> findPageBySpec(WorkingProcedureClassParam param);

}
