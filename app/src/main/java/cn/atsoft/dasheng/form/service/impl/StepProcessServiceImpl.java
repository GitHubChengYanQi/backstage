package cn.atsoft.dasheng.form.service.impl;

import cn.atsoft.dasheng.form.entity.ActivitiSetpSet;
import cn.atsoft.dasheng.form.entity.ActivitiSetpSetDetail;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.form.model.result.ActivitiSetpSetDetailResult;
import cn.atsoft.dasheng.form.model.result.ActivitiSetpSetResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.production.entity.ProcessRoute;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.sun.org.apache.bcel.internal.generic.ANEWARRAY;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StepProcessServiceImpl implements StepProcessService {
    @Autowired
    private StepsService stepsService;
    @Autowired
    private ActivitiSetpSetDetailService setDetailService;


    @Override
    public List<ActivitiSetpSetDetail> getSetDetailSByRouId(Long processRouteId) {

        List<ActivitiSteps> steps = stepsService.query().eq("from_id", processRouteId).list();
        List<ActivitiSetpSetDetail> setDetails = setDetailService.list();
        return getDetail(steps, setDetails);

    }


    private List<ActivitiSetpSetDetail> getDetail(List<ActivitiSteps> stepsResults, List<ActivitiSetpSetDetail> setpSetDetails) {

        for (ActivitiSteps result : stepsResults) {
            if (result.getStepType().equals("setp")) {
                return getSetDetail(result.getSetpsId(), setpSetDetails);
            }

        }
        return new ArrayList<>();
    }


    private List<ActivitiSetpSetDetail> getSetDetail(Long setpId, List<ActivitiSetpSetDetail> data) {
        List<ActivitiSetpSetDetail> details = new ArrayList<>();
        for (ActivitiSetpSetDetail datum : data) {
            if (datum.getSetpsId().equals(setpId)) {
                details.add(datum);
            }
        }
        return details;
    }

}
