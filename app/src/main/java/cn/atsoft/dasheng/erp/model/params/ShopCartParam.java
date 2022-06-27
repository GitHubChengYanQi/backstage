package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.erp.pojo.PositionNum;
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
 * 购物车
 * </p>
 *
 * @author song
 * @since 2022-06-06
 */
@Data
@ApiModel
public class ShopCartParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private List<ShopCartParam> shopCartParams;

    private List<PositionNum> positionNums;

    private List<String> types;

    private String storehousePositionsId;

    private Long sourceId;

    private List<Long> ids;

    @ApiModelProperty("")
    private Long cartId;

    private Long formId;

    private Long formStatus;

    private Long instockListId;


    /**
     * 类型
     */
    @ApiModelProperty("类型")
    @NotNull(message = "请输入类型")
    private String type;

    @ApiModelProperty("")
    private Long skuId;

    @ApiModelProperty("")
    private Long brandId;

    @ApiModelProperty("")
    private Long customerId;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer status;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Long number;

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
