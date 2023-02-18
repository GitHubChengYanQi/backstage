package cn.atsoft.dasheng.form.model;

import cn.atsoft.dasheng.form.model.enums.ModelEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ModelProcessDao {
//    模块
    private String model;

//    模块名
    private String modelName;

    @ApiModelProperty("是否为审批流程")
    private Boolean isAudit;

//    步骤详情
    List<ProcessStepTypeDao> stepTypes;
//    包含类别
    List <ModelType> types;
}


