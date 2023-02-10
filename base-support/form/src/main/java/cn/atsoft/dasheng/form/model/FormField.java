package cn.atsoft.dasheng.form.model;

import cn.atsoft.dasheng.form.model.result.FormFieldLink;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class FormField {


    @ApiModelProperty("名称")
    private String title;

    @ApiModelProperty("字段名")
    private String name;
    @ApiModelProperty("字段名")
    private String field;

    @ApiModelProperty("模块key")
    private FormFieldEnum type;

    @ApiModelProperty("需要请求的数据参数格式")
    private FormFieldDataSource dataSource;

    @ApiModelProperty("联动")
    List<FormFieldLink> links;


//    @ApiModelProperty("下级数据")
//    private List<FormField> children;



}
