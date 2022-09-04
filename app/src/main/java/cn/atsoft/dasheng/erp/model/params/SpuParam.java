package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.base.dict.AbstractDictMap;
import cn.atsoft.dasheng.erp.entity.AttributeValues;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import cn.atsoft.dasheng.erp.service.AttributeValuesService;
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
 *
 * </p>
 *
 * @author
 * @since 2021-10-18
 */
@Data
@ApiModel
public class SpuParam extends AbstractDictMap implements Serializable, BaseValidatingParam {
    private Boolean isHidden;
    private static final long serialVersionUID = 1L;
    private SpuRequest spuAttributes;
    private Long spuStandard;
    private Long spuClass;
    private SpuClassificationParam spuClassification;
//    private List<ItemAttributeParam> spuAttributes;

    private String specifications;

    /**
     * 类型
     */
    private Integer type;
    /**
     * 分类id
     */

    private Long spuClassificationId;

    private List<SpuRequest> spuRequests;
    /**
     * 物品Id
     */
    @ApiModelProperty("物品Id")
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
    @ApiModelProperty("属性")
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

    @Override
    public String checkParam() {
        return null;
    }

    @Override
    public void init() {

    }

    @Override
    protected void initBeWrapped() {

    }
}
