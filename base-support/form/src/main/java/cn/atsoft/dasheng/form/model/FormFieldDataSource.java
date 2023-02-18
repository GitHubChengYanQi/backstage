package cn.atsoft.dasheng.form.model;

import cn.atsoft.dasheng.form.model.enums.FormFieldDataSourseType;
import cn.atsoft.dasheng.form.model.result.FormFieldValue;
import cn.atsoft.dasheng.form.pojo.AuditType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class FormFieldDataSource {
    @ApiModelProperty("是否需要请求接口获取数据")
    FormFieldDataSourseType type;
    @ApiModelProperty("如果有请求  填写请求地址")
    String apiUrl;
    @ApiModelProperty("显示文字")
    String name;
    @ApiModelProperty("值")
    List<FormFieldValue> values;
    @ApiModelProperty("值")
    Object value;

}
