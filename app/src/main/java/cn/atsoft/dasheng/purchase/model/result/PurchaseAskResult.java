package cn.atsoft.dasheng.purchase.model.result;

import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 采购申请
 * </p>
 *
 * @author song
 * @since 2021-12-15
 */
@Data
@ApiModel
public class PurchaseAskResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private String CreateUserName;
    List<PurchaseListingResult> purchaseListingResults;

    /**
     * 采购申请id
     */
    @ApiModelProperty("采购申请id")
    private Long purchaseAskId;

    /**
     * 编号
     */
    @ApiModelProperty("编号")
    private String coding;

    /**
     * 采购类型
     */
    @ApiModelProperty("采购类型")
    private String type;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String note;

    /**
     * 申请状态
     */
    @ApiModelProperty("申请状态")
    private Integer status;

    /**
     * 总金额
     */
    @ApiModelProperty("总金额")
    private Integer money;

    /**
     * 总数量
     */
    @ApiModelProperty("总数量")
    private Long number;

    /**
     * 种类数量
     */
    @ApiModelProperty("种类数量")
    private Long typeNumber;

    /**
     * 来源
     */
    @ApiModelProperty("来源")
    private String source;

    /**
     * 来源id
     */
    @ApiModelProperty("来源id")
    private Long sourceId;

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
     * 创建用户
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改用户
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Long display;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
