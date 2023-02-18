package cn.atsoft.dasheng.form.model;

import cn.atsoft.dasheng.form.model.enums.FormFieldEnum;
import cn.atsoft.dasheng.form.model.result.FormFieldLink;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FormField {


    @ApiModelProperty("名称")
    private String title;

    @ApiModelProperty("字段名")
    private String name;

    @ApiModelProperty("模块key")
    private String type;

    @ApiModelProperty("需要请求的数据参数格式")
    private FormFieldDataSource dataSource;

    @ApiModelProperty("")
    private String key;


    @ApiModelProperty("规则")
    private FieldRule fieldRule;

    @ApiModelProperty("是否必填")
    private Boolean required;

    @ApiModelProperty("联动")
    List<FormFieldLink> links = new ArrayList<>();


//    @ApiModelProperty("下级数据")
//    private List<FormField> children;



}
