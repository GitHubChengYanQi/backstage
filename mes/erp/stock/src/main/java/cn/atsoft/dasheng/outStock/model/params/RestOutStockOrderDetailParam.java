package cn.atsoft.dasheng.outStock.model.params;

import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
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
public class RestOutStockOrderDetailParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private Long storehousePositionsId;
    private List<Long> storehousePositionsIds;
    private Long storehouseId;
    private Long cartId;
    private Long pickListsCart;
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Integer receivedNumber;
    private Integer status;
    private Long inkindId;
    /**
     * 子表id
     */
    @ApiModelProperty("子表id")
    private Long pickListsDetailId;


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

    /**
     * 品牌
     */
    @ApiModelProperty("品牌")
    private Long brandId;

    @ApiModelProperty("")
    private Integer number;

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
