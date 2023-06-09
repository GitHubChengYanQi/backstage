package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.erp.entity.InstockOrder;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
public class AnomalyOrderResult implements Serializable {

    private Integer positionNum;

    private Integer total;

    private Integer handle;

    private Integer skuNumber;

    private User masterUser;

    private static final long serialVersionUID = 1L;

    private InstockOrder instockOrder;

    private List<AnomalyResult> anomalyResults;

    private String statusName;

    private String type;

    private Long instockOrderId;

    private Integer complete;

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
}
