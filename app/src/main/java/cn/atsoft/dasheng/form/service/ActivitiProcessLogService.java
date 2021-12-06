package cn.atsoft.dasheng.form.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiProcessLog;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessLogParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessLogResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 流程日志表 服务类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
public interface ActivitiProcessLogService extends IService<ActivitiProcessLog> {

    /**
     * 新增
     *
     * @author Sing
     * @Date 2021-11-10
     */
//    void add(Long param, Integer status);
    void audit(Long param, Integer status);
    Boolean checkUser(AuditRule starUser);

    /**
     * 删除
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void delete(ActivitiProcessLogParam param);

    /**
     * 更新
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void update(ActivitiProcessLogParam param);

    List<ActivitiProcessLog> getAudit(Long taskId);

    ActivitiStepsResult addLog(Long processId, Long taskId);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    ActivitiProcessLogResult findBySpec(ActivitiProcessLogParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    List<ActivitiProcessLogResult> findListBySpec(ActivitiProcessLogParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    PageInfo<ActivitiProcessLogResult> findPageBySpec(ActivitiProcessLogParam param);

}
