package cn.atsoft.dasheng.form.model.enums;

import io.swagger.annotations.ApiModelProperty;

public enum FormFieldEnum {
    @ApiModelProperty("自动完成")
    AutoComplete("自动完成"),
    @ApiModelProperty("单选框")
    Radio("单选框"),
    @ApiModelProperty("多选框")
    Checkbox("多选框"),
    @ApiModelProperty("输入框")
    Input("输入框"),
    @ApiModelProperty("数字输入框")
    InputNumber("数字输入框"),
    @ApiModelProperty("选择器")
    Select("选择器"),
    @ApiModelProperty("联级选择")
    Cascader("联级选择"),
    //
    @ApiModelProperty("审批节点类型（审批/状态）")
    StepsType("类型"),

    @ApiModelProperty("流程人员选择")
    SelectUser("人员范围"),

    @ApiModelProperty("流程审批操作 并签或签 按钮")
    AuditOperation("流程审批操作 并签或签 按钮");

    private final String name;

    public String getName() {
        return name;
    }

    FormFieldEnum(String name) {
        this.name = name;
    }
}
