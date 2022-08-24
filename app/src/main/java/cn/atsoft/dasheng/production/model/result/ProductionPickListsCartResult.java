package cn.atsoft.dasheng.production.model.result;

import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.model.result.StorehouseSimpleResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
public class ProductionPickListsCartResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private SkuSimpleResult skuResult;
    private List<Long> brandIds;
    private List<String> brandNames;
    private ProductionPickListsResult productionPickListsResult;
    private ProductionPickListsResult pickListsResult;
    private ProductionPickListsDetailResult productionPickListsDetailResult;

    private Long stockNumber;
    //仓库
    private StorehouseSimpleResult storehouseResult;
    //库位
    private StorehousePositionsResult storehousePositionsResult;


    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long brandId;
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long customerId;
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long storehousePositionsId;
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long storehouseId;

    private BrandResult brandResult;
    @ApiModelProperty("类型")
    private String type;
    /**
     * 购物车id
     */
    @ApiModelProperty("购物车id")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long pickListsCart;

    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long inkindId;

    /**
     * 主表id
     */
    @ApiModelProperty("主表id")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long pickListsId;

    /**
     * 子表id
     */
    @ApiModelProperty("子表id")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long pickListsDetailId;

    /**
     * 物料id
     */
    @ApiModelProperty("物料id")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long skuId;

    @ApiModelProperty("")
    private Integer number;

    @ApiModelProperty("")
    @JSONField(serialize = false )
    private Integer status;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false )
    private Long createUser;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false )
    private Long updateUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false )
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false )
    private Date updateTime;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    @JSONField(serialize = false )
    private Integer display;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
