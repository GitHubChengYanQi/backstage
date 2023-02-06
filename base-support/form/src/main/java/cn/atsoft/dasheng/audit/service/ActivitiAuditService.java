package cn.atsoft.dasheng.audit.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.audit.entity.ActivitiAudit;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.audit.model.params.ActivitiAuditParam;
import cn.atsoft.dasheng.audit.model.result.ActivitiAuditResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 审批配置表 服务类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
public interface ActivitiAuditService extends IService<ActivitiAudit> {

    /**
     * 新增
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void add(ActivitiAuditParam param);

    List<ActivitiAudit> getListBySteps(List<ActivitiSteps> steps);

    List<ActivitiAudit> getListByStepsId(List<Long> stepsId);

    /**
     * 删除
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void delete(ActivitiAuditParam param);


    ActivitiAuditResult getAudit(Long id);

    /**
     * 更新
     *
     * @author Sing
     * @Date 2021-11-10
     */
    void update(ActivitiAuditParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    ActivitiAuditResult findBySpec(ActivitiAuditParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    List<ActivitiAuditResult> findListBySpec(ActivitiAuditParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Sing
     * @Date 2021-11-10
     */
    PageInfo findPageBySpec(ActivitiAuditParam param);


    List<ActivitiAuditResult> backAudits(List<Long> ids);


    List<Long> getUserIds(Long taskId);
    void power(ActivitiProcess activitiProcess);
}
