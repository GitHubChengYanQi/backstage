package cn.atsoft.dasheng.supplier.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 供应商黑名单
 * </p>
 *
 * @author Captian_Jazz
 * @since 2021-12-20
 */
@Data
@ApiModel
public class SupplierBlacklistResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 黑名单id
     */
    @ApiModelProperty("黑名单id")
    private Long blackListId;

    /**
     * 供应商id
     */
    @ApiModelProperty("供应商id")
    private Long supplierId;

    /**
     * 备注（原因）
     */
    @ApiModelProperty("备注（原因）")
    private String remark;

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
