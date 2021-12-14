package cn.atsoft.dasheng.erp.pojo;

import cn.atsoft.dasheng.erp.entity.QualityTaskDetail;
import cn.atsoft.dasheng.erp.entity.QualityTaskRefuse;
import cn.atsoft.dasheng.erp.model.result.QualityTaskDetailResult;
import cn.atsoft.dasheng.erp.model.result.QualityTaskRefuseResult;
import cn.atsoft.dasheng.erp.model.result.QualityTaskResult;
import lombok.Data;

import java.util.List;

@Data
public class TaskDetail {
    private List<QualityTaskDetailResult> detailResults;
    private List<QualityTaskRefuseResult> refuseResults;
}
