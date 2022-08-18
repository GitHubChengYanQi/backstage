package cn.atsoft.dasheng.form.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiProcessLog;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.form.model.params.ActivitiSetpSetParam;
import cn.atsoft.dasheng.form.model.params.ActivitiStepsParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessLogResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 流程步骤表 服务类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
public interface ActivitiStepsService extends IService<ActivitiSteps> {

    /**
     * 新增
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void addProcess(ActivitiStepsParam param);

    /**
     * 删除
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void delete(ActivitiStepsParam param);

    /**
     * 更新
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void update(ActivitiStepsParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    ActivitiStepsResult findBySpec(ActivitiStepsParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    List<ActivitiStepsResult> findListBySpec(ActivitiStepsParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    PageInfo findPageBySpec(ActivitiStepsParam param);


    ActivitiStepsResult backStepsResult(Long id);


    ActivitiStepsResult getSteps(Long id);


    List<ActivitiStepsResult> backSteps(List<Long> ids);

    Boolean checkUser(AuditRule starUser);


    ActivitiStepsResult getStepResult(Long processId);


    List<ActivitiStepsResult> getStepsByProcessId(Long processId);

    /**
     * 流程结构返回状态
     *
     * @param
     * @param logs
     * @return
     */
    void  getStepLog(ActivitiStepsResult stepResult, List<ActivitiProcessLogResult> logs);

    ActivitiStepsResult getStepResultByType(String type);

}
