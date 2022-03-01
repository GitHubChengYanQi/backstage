package cn.atsoft.dasheng.crm.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 付款记录
 * </p>
 *
 * @author song
 * @since 2022-03-01
 */
@Data
@ApiModel
public class PaymentRecordParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 付款记录id
     */
    @ApiModelProperty("付款记录id")
    private Long recordId;

    /**
     * 付款详情id
     */
    @ApiModelProperty("付款详情id")
    @NotNull
    private Long detailId;

    /**
     * 付款主表id
     */
    @ApiModelProperty("付款主表id")
    private Long paymentId;

    /**
     * 付款金额
     */
    @ApiModelProperty("付款金额")
    @NotNull
    private Integer paymentAmount;

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
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
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
