package cn.atsoft.dasheng.orderPaymentApply.model.result;

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
 * @author Captain_Jazz
 * @since 2023-03-18
 */
@Data
@ApiModel
public class CrmOrderPaymentApplyResult implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private Long wxAuditBind;

    @ApiModelProperty("")
    private Long orderId;

    @ApiModelProperty("")
    private Long newMoney;

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

    @ApiModelProperty("")
    private String spNo;

    @ApiModelProperty("")
    private String status;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
