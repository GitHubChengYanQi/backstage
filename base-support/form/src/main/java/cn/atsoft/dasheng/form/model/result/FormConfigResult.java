package cn.atsoft.dasheng.form.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 自定义表单
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@Data
@ApiModel
public class FormConfigResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 表单Id
     */
    @ApiModelProperty("表单Id")
    private Long formId;

    /**
     * 表单类型：main（主表），son（子表）
     */
    @ApiModelProperty("表单类型：main（主表），son（子表）")
    private Integer type;

    /**
     * 表单名称
     */
    @ApiModelProperty("表单名称")
    private String formName;

    /**
     * 分类Id
     */
    @ApiModelProperty("分类Id")
    private Long categoryId;

    /**
     * 备注说明
     */
    @ApiModelProperty("备注说明")
    private String remarks;

    @ApiModelProperty("")
    private Integer display;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Date updateTime;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
