package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.app.entity.Material;
import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.model.params.Attribute;
import cn.atsoft.dasheng.app.model.result.UnitResult;
import cn.atsoft.dasheng.appBase.aop.DocumentFieldAop;
import cn.atsoft.dasheng.erp.entity.Category;
import cn.atsoft.dasheng.erp.model.params.CategoryRequest;
import cn.atsoft.dasheng.erp.model.params.SkuJson;
import cn.atsoft.dasheng.erp.model.params.SkuRequest;
import cn.atsoft.dasheng.erp.model.params.SpuRequest;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

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

    private UnitResult unitResult;

    private static final long serialVersionUID = 1L;
    /**
     * 类型
     */
    private Integer type;

    private cn.atsoft.dasheng.erp.model.result.CategoryResult categoryResult;


    private Category category;


    private List<CategoryRequest> categoryRequests;


    private SkuRequest sku;


    private Material material;

    private SpuClassificationResult spuClassificationResult;

    /**
     * 分类id
     */
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long spuClassificationId;

    /**
     * 物品Id
     */
    @ApiModelProperty("物品Id")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long spuId;

    /**
     * 产品码
     */
    @ApiModelProperty("产品码")
    private String coding;

    /**
     * 型号
     */
    @ApiModelProperty("型号")
    private String model;
    /**
     * 物品名字
     */
    @DocumentFieldAop(value = true)
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
    @JSONField(serializeUsing= ToStringSerializer.class)
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

//    @JSONField(serialize = false)
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

    /**
     * 部门编号
     */
    @JSONField(serialize = false)
    @ApiModelProperty("部门编号")
    private Long deptId;

    /**
     * 产品分类
     */
    @ApiModelProperty("产品分类")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long classId;

    /**
     * 单位
     */
    @ApiModelProperty("单位")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long unitId;

    /**
     * 类目id
     */
    @ApiModelProperty("类目id")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long categoryId;

    /**
     * 属性id
     */
    @ApiModelProperty("属性id")
    private String attribute;

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
