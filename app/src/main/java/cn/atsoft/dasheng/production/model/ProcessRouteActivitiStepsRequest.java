package cn.atsoft.dasheng.production.model;

import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.production.entity.ProcessRoute;
import cn.atsoft.dasheng.production.model.result.ProcessRouteResult;
import lombok.Data;

import java.util.List;

@Data
public class ProcessRouteActivitiStepsRequest {
    private ProcessRouteResult processRouteResult;
    private List<ActivitiStepsResult> stepsResults;
}
