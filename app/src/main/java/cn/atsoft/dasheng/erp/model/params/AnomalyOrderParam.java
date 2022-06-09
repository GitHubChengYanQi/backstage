package cn.atsoft.dasheng.erp.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author song
 * @since 2022-06-09
 */
@Data
@ApiModel
public class AnomalyOrderParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    private List<AnomalyParam> anomalyParams;

    @ApiModelProperty("")
    private Long orderId;

    @ApiModelProperty("")
    private String coding;

    @ApiModelProperty("")
    private Long status;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty("")
    private Integer display;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
