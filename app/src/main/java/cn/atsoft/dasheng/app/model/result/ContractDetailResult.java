package cn.atsoft.dasheng.app.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 合同产品明细
 * </p>
 *
 * @author sb
 * @since 2021-09-18
 */
@Data
@ApiModel
public class ContractDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private ItemsResult itemsResult;
    private BrandResult brandResult;


    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    private Long brandId;

    /**
     * 合同id
     */
    @ApiModelProperty("合同id")
    private Long contractId;

    /**
     * 物品id
     */
    @ApiModelProperty("物品id")
    private Long itemId;

    /**
     * 物品数量
     */
    @ApiModelProperty("物品数量")
    private Integer quantity;

    /**
     * 流程状态
     */
    @ApiModelProperty("流程状态")
    private Long processId;

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
     * 销售单价
     */
    @ApiModelProperty("销售单价")
    private Integer salePrice;

    /**
     * 总计
     */
    @ApiModelProperty("总计")
    private Integer totalPrice;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
