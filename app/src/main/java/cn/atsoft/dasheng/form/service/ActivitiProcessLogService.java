package cn.atsoft.dasheng.form.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiAudit;
import cn.atsoft.dasheng.form.entity.ActivitiProcessLog;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessLogParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessLogResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.pojo.RuleType;
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

    List<ActivitiProcessLog> listByTaskId(Long taskId);

    /**
     * 新增
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void audit(Long param, Integer status);

//    void audit(Long taskId, Integer status, String type, Integer auto);

    void autoAudit(Long taskId, Integer status, Long loginUserId);


    void checkAction(Long id, String formType, Long actionId, Long loginUserId);


    void checkLogActionComplete(Long taskId, Long stepId, Long actionId, Long loginUserId);

    Boolean checkUser(AuditRule starUser, Long taskId);

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

    ActivitiStepsResult addLog(Long processId, Long taskId, Integer status, Long loginUserId);

    ActivitiStepsResult microAddLog(Long processId, Long taskId, Long userId);

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

    /**
     * 判断状态
     *
     * @return
     */
    Boolean judgeStatus(ActivitiProcessTask task, RuleType ruleType);


    List<ActivitiProcessLogResult> getLogByTaskProcess(Long processId, Long taskId);

    void addLogJudgeBranch(Long processId, Long taskId, Long sourId, String type);

    /**
     * 取当前任务的log和规则
     *
     * @param taskId
     * @return
     */
    List<ActivitiProcessLogResult> getLogAudit(Long taskId);

    ActivitiAudit getRule(List<ActivitiAudit> activitiAudits, Long stepId);

    /**
     * 查询未审核
     *
     * @author Sing
     * @Date 2021-11-10
     */
    List<ActivitiProcessLogResult> auditList(ActivitiProcessLogParam param);

    List<ActivitiProcessLogResult> sendList(ActivitiProcessLogParam param);

    void judgeLog(Long taskId, List<Long> logIds);

    List<ActivitiProcessLog> getAuditByForm(Long formId, String type);
}
