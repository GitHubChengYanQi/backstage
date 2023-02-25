package cn.atsoft.dasheng.form.model.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FormFieldValue {
    @ApiModelProperty("名称")
    private String label;

    @ApiModelProperty("值")
    private Object value;
}
