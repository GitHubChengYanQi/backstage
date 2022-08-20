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
 * 入库记录单
 * </p>
 *
 * @author song
 * @since 2022-08-11
 */
@Data
@ApiModel
public class InstockReceiptParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
    private List<String> times;

    /**
     * 物品编号
     */
    @ApiModelProperty("物品编号")
    private Long receiptId;

    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String coding;

    /**
     * 入库单
     */
    @ApiModelProperty("入库单")
    private Long instockOrderId;

    /**
     * 审批人
     */
    @ApiModelProperty("审批人")
    private String auditPerson;

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
