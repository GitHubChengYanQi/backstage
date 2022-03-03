package cn.atsoft.dasheng.form.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;

import cn.atsoft.dasheng.form.model.params.ActivitiStepsParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessLogResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.pojo.ViewUpdate;
import cn.atsoft.dasheng.production.model.params.ProcessRouteParam;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 流程步骤表 服务类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
public interface StepsService extends IService<ActivitiSteps> {



    Long add(ActivitiStepsParam param);

    Long addProcessRoute(ProcessRouteParam param);


    ActivitiStepsResult detail(Long formId);

    ViewUpdate getProcessTime(Long fromId);
}
