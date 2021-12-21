package cn.atsoft.dasheng.purchase.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 采购配置表
 * </p>
 *
 * @author song
 * @since 2021-12-21
 */
@Data
@ApiModel
public class PurchaseConfigResult implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private Long purchaseConfigId;

    /**
     * 类型（最低级别：level，是否是供应商：supply）
     */
    @ApiModelProperty("类型（最低级别：level，是否是供应商：supply）")
    private String type;

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
