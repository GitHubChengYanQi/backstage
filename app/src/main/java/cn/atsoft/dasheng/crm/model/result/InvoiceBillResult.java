package cn.atsoft.dasheng.crm.model.result;

import cn.atsoft.dasheng.appBase.entity.Media;
import cn.atsoft.dasheng.appBase.model.result.MediaUrlResult;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 发票
 * </p>
 *
 * @author song
 * @since 2022-04-22
 */
@Data
@ApiModel
public class InvoiceBillResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 发票id
     */
    @ApiModelProperty("发票id")
    private Long invoiceBillId;

    /**
     * 金额
     */
    @ApiModelProperty("金额")
    private Integer money;

    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private Long enclosureId;

    private MediaUrlResult mediaUrlResult;

    /**
     * 发票名称
     */
    @ApiModelProperty("发票名称")
    private String name;

    /**
     * 发票日期
     */
    @ApiModelProperty("发票日期")
    private Date invoiceDate;

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

    /**
     * 关联订单
     */
    @ApiModelProperty("关联订单")
    private Long orderId;

    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String coding;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
