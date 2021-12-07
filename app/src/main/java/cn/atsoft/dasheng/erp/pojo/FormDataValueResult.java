package cn.atsoft.dasheng.erp.pojo;

import cn.atsoft.dasheng.erp.model.request.FormValues;
import cn.atsoft.dasheng.erp.model.result.QualityPlanDetailResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 自定义表单各个字段数据
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@Data
@ApiModel
public class FormDataValueResult implements Serializable {

    private static final long serialVersionUID = 1L;


    private FormValues.DataValues dataValues;

    private QualityPlanDetailResult qualityPlanDetailResult;
    /**
     * 主键Id
     */
    @ApiModelProperty("主键Id")
    private Long valueId;

    /**
     * 数据Id
     */
    @ApiModelProperty("数据Id")
    private Long dataId;

    /**
     * 字段
     */
    @ApiModelProperty("字段")
    private Long field;

    /**
     * 值
     */
    @ApiModelProperty("值")
    private String value;

    /**
     * 删除状态
     */
    @ApiModelProperty("删除状态")
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
