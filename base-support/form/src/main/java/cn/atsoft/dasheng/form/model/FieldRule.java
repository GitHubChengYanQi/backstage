package cn.atsoft.dasheng.form.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FieldRule {
    // 内置校验规则，参考string内置校验规则
    @ApiModelProperty("内置校验规则，参考string内置校验规则")
    private String format;

    // 自定义校验规则
    @ApiModelProperty("自定义校验规则")
    private String validator;

//    // 是否必填
//    @ApiModelProperty("是否必填")
//    private String required;

    // 匹配规则
    @ApiModelProperty("匹配规则")
    private String pattern;

    // 最大长度
    @ApiModelProperty("最大长度")
    private String max;

    // 最大值（大于）
    @ApiModelProperty("最大值（大于）")
    private String maximum;

    // 最大值（大于等于）
    @ApiModelProperty("最大值（大于等于）")
    private String exclusiveMaximum;

    // 最小值（小于等于）
    @ApiModelProperty("最小值（小于等于）")
    private String exclusiveMinimum;

    // 最小值（小于）
    @ApiModelProperty("最小值（小于）")
    private String minimum;

    // 最小长度
    @ApiModelProperty("最小长度")
    private String min;

    // 长度
    @ApiModelProperty("长度")
    private String len;

    // 空格
    @ApiModelProperty("空格")
    private String whitespace;

    // 是否包含在枚举列表中
    @ApiModelProperty("是否包含在枚举列表中")
    private String isEnum;

    // 错误信息
    @ApiModelProperty("错误信息")
    private String message;
}
