package cn.atsoft.dasheng.form.service;

import cn.atsoft.dasheng.form.entity.ActivitiSetpSetDetail;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.form.model.result.ActivitiSetpSetResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;

import java.util.List;

public interface StepProcessService {


    List<ActivitiSetpSetDetail> getSetDetailSByRouId(Long processRouteId);
}
