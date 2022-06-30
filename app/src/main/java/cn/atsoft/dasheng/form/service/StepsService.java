package cn.atsoft.dasheng.form.service;

import cn.atsoft.dasheng.form.entity.ActivitiSteps;

import cn.atsoft.dasheng.form.model.params.StepsParam;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.pojo.ViewUpdate;
import cn.atsoft.dasheng.production.model.params.ProcessRouteParam;
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
public interface StepsService extends IService<ActivitiSteps> {



    Long add(StepsParam param);

    Long addProcessRoute(ProcessRouteParam param);


    ActivitiStepsResult detail(Long formId);

    List<ActivitiStepsResult> getStepsResultByFormId(Long formId);

    ViewUpdate getProcessTime(Long fromId);

    void headPortrait(ActivitiStepsResult stepResult);

    String imgUrl(String userId);
}
