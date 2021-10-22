package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.app.model.result.UnitResult;
import cn.atsoft.dasheng.erp.entity.Category;
import cn.atsoft.dasheng.erp.model.params.CategoryRequest;
import cn.atsoft.dasheng.erp.model.params.SkuJson;
import cn.atsoft.dasheng.erp.model.params.SkuRequest;
import cn.atsoft.dasheng.erp.model.params.SpuRequest;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since 2021-10-18
 */
@Data
@ApiModel
public class SpuResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Category category;

    private UnitResult unitResult;

    private List<CategoryRequest> categoryRequests;

    private List<AttributeValuesResult> itemAttributeResults;

    private SkuRequest spuAttributes;

    /**
     * 物品Id
     */
    @ApiModelProperty("物品Id")
    private Long spuId;

    /**
     * 物品名字
     */
    @ApiModelProperty("物品名字")
    private String name;

    /**
     * 质保期
     */
    @ApiModelProperty("质保期")
    private Integer shelfLife;

    /**
     * 物品库存
     */
    @ApiModelProperty("物品库存")
    private Integer inventory;

    /**
     * 生产日期
     */
    @ApiModelProperty("生产日期")
    private Date productionTime;

    /**
     * 重要程度
     */
    @ApiModelProperty("重要程度")
    private Integer important;

    /**
     * 物品重量
     */
    @ApiModelProperty("物品重量")
    private Integer weight;

    /**
     * 材质id
     */
    @ApiModelProperty("材质id")
    private Long materialId;

    /**
     * 成本
     */
    @ApiModelProperty("成本")
    private Integer cost;

    /**
     * 易损
     */
    @ApiModelProperty("易损")
    private Integer vulnerability;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

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

    /**
     * 产品分类
     */
    @ApiModelProperty("产品分类")
    private Long classId;

    /**
     * 单位
     */
    @ApiModelProperty("单位")
    private Long unitId;

    /**
     * 类目id
     */
    @ApiModelProperty("类目id")
    private Long categoryId;

    /**
     * 属性id
     */
    @ApiModelProperty("属性id")
    private Long attributeId;

    /**
     * 养护周期值
     */
    @ApiModelProperty("养护周期值")
    private Long curingCycle;

    /**
     * 养护周期类型（年|月|日）
     */
    @ApiModelProperty("养护周期类型（年|月|日）")
    private Integer cycleType;

    /**
     * 生产类型（自制|委外|外采购）
     */
    @ApiModelProperty("生产类型（自制|委外|外采购）")
    private Integer productionType;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
