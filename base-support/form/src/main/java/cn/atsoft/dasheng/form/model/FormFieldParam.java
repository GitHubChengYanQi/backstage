package cn.atsoft.dasheng.form.model;

import cn.atsoft.dasheng.form.model.result.FormFieldLink;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@ApiModel
@Data
public class FormFieldParam {


    @ApiModelProperty("字段名")
    private String name;

    @ApiModelProperty("值")
    private Object value;



}
