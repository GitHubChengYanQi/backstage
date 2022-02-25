package cn.atsoft.dasheng.template.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 付款模板
 * </p>
 *
 * @author song
 * @since 2022-02-24
 */
@Data
@ApiModel
public class PaymentTemplateParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private List<PaymentTemplateDetailParam> templates;
    /**
     * 模板主见
     */
    @ApiModelProperty("模板主见")
    private Long templateId;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 是否常用
     */
    @ApiModelProperty("是否常用")
    private Integer oftenUser;

    @ApiModelProperty("")
    private Long pid;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
