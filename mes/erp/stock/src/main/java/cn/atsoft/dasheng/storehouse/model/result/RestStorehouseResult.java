package cn.atsoft.dasheng.storehouse.model.result;

//import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
//import cn.atsoft.dasheng.production.model.request.SkuInStockTree;
import cn.atsoft.dasheng.goods.category.model.result.RestCategoryResult;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 地点表
 * </p>
 *
 * @author
 * @since 2021-07-15
 */
@Data
@ApiModel
public class RestStorehouseResult implements Serializable {
//    private SkuInStockTree skuInStockTree;

    private static final long serialVersionUID = 1L;
    List<RestStorehouseResult> childrenList;
    private String description;
    private List<RestCategoryResult>spuClassResults;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    private Long pid;
    private Integer sort;


//    private List<StorehousePositionsResult> storehousePositionsResults;
    /**
     * 经度
     */

    private BigDecimal longitude;

    /**
     * 仓库id
     */

    @ApiModelProperty("仓库id")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long storehouseId;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    private String coding;


    /**
     * 位置
     */

    private String palce;

    /**
     * 面积
     */
    @ApiModelProperty("面积")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long measure;

    /**
     * 容量
     */
    @ApiModelProperty("容量")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long capacity;

    /**
     * 创建时间
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 创建者
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改时间
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 修改者
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 状态
     */
    @JSONField(serialize = false)
    @ApiModelProperty("状态")
    private Integer display;
    private String children;

    private String childrens;

    @JSONField(serialize = false)
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
    @JSONField(serialize = false)
    private Long deptId;
}
