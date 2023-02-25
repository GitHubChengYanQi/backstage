package cn.atsoft.dasheng.form.model;

import cn.atsoft.dasheng.form.model.enums.ProcessStepTypeEnum;
import lombok.Data;

@Data
public  class ProcessStepTypeDao{
//     步骤类型
    private ProcessStepTypeEnum type;
//     类别
    private String key;
//     类别名
    private String typeName;

}
