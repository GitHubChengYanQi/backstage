package cn.atsoft.dasheng.production.model.params;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 领料单详情表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-25
 */
@Data
@ApiModel
public class ProductionPickListsCartParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
    private List<Long> cartIds;
    private List<Long> pickListsIds;

    private List<ProductionPickListsCartParam> productionPickListsCartParams;



    private Long storehousePositionsId;
    private Long storehouseId;

    /**
     * 子表id
     */
    @ApiModelProperty("子表id")
    private Long pickListsCart;

    /**
     * 主表id
     */
    @ApiModelProperty("主表id")

    private Long pickListsId;
    /**
     * 物料id
     */
    @ApiModelProperty("物料id")
    private Long skuId;

    @ApiModelProperty("")
    private Integer number;

    @ApiModelProperty("")
    private Integer status;

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
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}