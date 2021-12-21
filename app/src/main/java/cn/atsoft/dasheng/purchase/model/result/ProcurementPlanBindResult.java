package cn.atsoft.dasheng.purchase.model.result;

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
 * @since 2021-12-21
 */
@Data
@ApiModel
public class ProcurementPlanBindResult implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private Long detailId;

    /**
     * 采购计划id
     */
    @ApiModelProperty("采购计划id")
    private Long procurementPlanId;

    /**
     * 申请单id
     */
    @ApiModelProperty("申请单id")
    private Long askId;

    /**
     * 申请单绑定详情id
     */
    @ApiModelProperty("申请单绑定详情id")
    private Long askDetailId;

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
