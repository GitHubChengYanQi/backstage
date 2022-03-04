package cn.atsoft.dasheng.form.service.impl;

import cn.atsoft.dasheng.core.util.ToolUtil;
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
import com.sun.org.apache.bcel.internal.generic.NEW;
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
        List<ActivitiStepsResult> stepsResults = stepsService.getStepsResultByFormId(processRouteId);   //当前工艺下所有步骤
        List<ActivitiSetpSetDetail> setDetails = setDetailService.list();
        for (ActivitiStepsResult stepsResult : stepsResults) {
            if (stepsResult.getSupper() == 0) {   //返回顶级
                return getDetail(stepsResult, stepsResults, setDetails);

            }
        }
        return new ArrayList<>();
    }


    /**
     * 获取所有工序
     *
     * @param stepsResults
     */
    private List<ActivitiSetpSetDetail> getDetail(ActivitiStepsResult stepsResult, List<ActivitiStepsResult> stepsResults, List<ActivitiSetpSetDetail> setpSetDetails) {

        List<ActivitiSetpSetDetail> details = new ArrayList<>();
        for (ActivitiStepsResult result : stepsResults) {
            if (ToolUtil.isNotEmpty(stepsResult.getChildrens()) && stepsResult.getChildren().equals(result.getSetpsId().toString())) {
                List<ActivitiSetpSetDetail> detail = new ArrayList<>();
                switch (result.getStepType()) {
                    case "setp":
                        detail = getSetDetail(result.getSetpsId(), setpSetDetails);
                        break;
                    case "ship":
                        detail = getSetDetailSByRouId(result.getFormId());
                        break;
                    case "route":
                        detail = getDetail(result, stepsResults, setpSetDetails);
                        break;
                }

                details.addAll(detail);
            }
        }
        return details;
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
