package cn.atsoft.dasheng.form.service.impl;

import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.service.ActivitiStepsService;
import cn.atsoft.dasheng.form.service.StepProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StepProcessServiceImpl implements StepProcessService {
    @Autowired
    private ActivitiStepsService stepsService;

    @Override
    public ActivitiStepsResult getStep(Long processRouteId) {


        return null;
    }
}
