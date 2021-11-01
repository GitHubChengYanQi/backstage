package cn.atsoft.dasheng.production.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.WorkingPorcedureClass;
import cn.atsoft.dasheng.production.model.params.WorkingPorcedureClassParam;
import cn.atsoft.dasheng.production.model.result.WorkingPorcedureClassResult;
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
public interface WorkingPorcedureClassService extends IService<WorkingPorcedureClass> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-10-29
     */
    void add(WorkingPorcedureClassParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-10-29
     */
    void delete(WorkingPorcedureClassParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-10-29
     */
    void update(WorkingPorcedureClassParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-10-29
     */
    WorkingPorcedureClassResult findBySpec(WorkingPorcedureClassParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-10-29
     */
    List<WorkingPorcedureClassResult> findListBySpec(WorkingPorcedureClassParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-10-29
     */
     PageInfo<WorkingPorcedureClassResult> findPageBySpec(WorkingPorcedureClassParam param);

}
