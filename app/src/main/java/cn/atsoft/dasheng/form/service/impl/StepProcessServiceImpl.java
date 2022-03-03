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
    private ActivitiSetpSetService setpSetService;
    @Autowired
    private ActivitiSetpSetDetailService setDetailService;


    @Override
    public List<ActivitiSetpSetResult> getStep(Long processRouteId) {
        List<ActivitiStepsResult> stepsResults = stepsService.getStepsResultByFormId(processRouteId);   //当前工艺下所有步骤

        for (ActivitiStepsResult stepsResult : stepsResults) {
            if (stepsResult.getSupper() == 0) {   //返回顶级
                return getAllSetId(stepsResults);
            }
        }
        return null;
    }

    /**
     * 获取所有工序
     *
     * @param stepsResults
     */
    private List<ActivitiSetpSetResult> getAllSetId(List<ActivitiStepsResult> stepsResults) {

        List<Long> stepIds = new ArrayList<>();
        for (ActivitiStepsResult stepsResult : stepsResults) {
            if (stepsResult.getStepType().equals("setp")) {
                stepIds.add(stepsResult.getSetpsId());
            }
        }
        return getAllSet(stepIds);
    }

    private List<ActivitiSetpSetResult> getAllSet(List<Long> ids) {
        List<ActivitiSetpSet> setpSets = setpSetService.query().in("setps_id", ids).list();
        List<ActivitiSetpSetResult> results = BeanUtil.copyToList(setpSets, ActivitiSetpSetResult.class, new CopyOptions());
        StepSetFormat(results);
        return results;
    }

    private void StepSetFormat(List<ActivitiSetpSetResult> data) {
        List<Long> setpIds = new ArrayList<>();
        for (ActivitiSetpSetResult datum : data) {
            setpIds.add(datum.getSetpsId());
        }
        List<ActivitiSetpSetDetail> setpSetDetails = setpIds.size() == 0 ? new ArrayList<>() : setDetailService.query().in("setps_id", setpIds).list();
        List<ActivitiSetpSetDetailResult> setpSetDetailResults = BeanUtil.copyToList(setpSetDetails, ActivitiSetpSetDetailResult.class, new CopyOptions());

        for (ActivitiSetpSetResult datum : data) {
            List<ActivitiSetpSetDetailResult> detailResults = new ArrayList<>();
            for (ActivitiSetpSetDetailResult setpSetDetailResult : setpSetDetailResults) {
                if (datum.getSetpsId().equals(setpSetDetailResult.getSetpsId())) {
                    detailResults.add(setpSetDetailResult);
                }
            }
            datum.setSetpSetDetails(detailResults);
        }
    }
}
