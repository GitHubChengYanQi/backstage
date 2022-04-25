package cn.atsoft.dasheng.erp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since 2021-10-18
 */
@TableName("goods_spu")
public class Spu implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 类型
     */
    @TableField("type")
    private Integer type;
    /**
     * 分类id
     */
    @TableField("spu_classification_id")
    private Long spuClassificationId;

    /**
     * 物品Id
     */
    @TableId(value = "spu_id", type = IdType.ID_WORKER)
    private Long spuId;
    /**
     * 型号
     */
    @TableField("model")

    private String model;


    /**
     * 物品名字
     */
    @TableField("name")
    private String name;


    /**
     * 产品码
     */
    @TableField("coding")
    private String coding;

    /**
     * 质保期
     */
    @TableField("shelf_life")
    private Integer shelfLife;

    /**
     * 物品库存
     */
    @TableField("inventory")
    private Integer inventory;

    /**
     * 生产日期
     */
    @TableField("production_time")
    private Date productionTime;

    /**
     * 重要程度
     */
    @TableField("important")
    private Integer important;

    /**
     * 物品重量
     */
    @TableField("weight")
    private Integer weight;

    /**
     * 材质id
     */
    @TableField("material_id")
    private Long materialId;

    /**
     * 成本
     */
    @TableField("cost")
    private Integer cost;

    /**
     * 易损
     */
    @TableField("vulnerability")
    private Integer vulnerability;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建者
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 修改者
     */
    @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

    /**
     * 状态
     */
    @TableField("display")
    private Integer display;

    /**
     * 部门编号
     */
    @TableField("deptId")
    private Long deptId;

    /**
     * 产品分类
     */
    @TableField("class_id")
    private Long classId;

    /**
     * 单位
     */
    @TableField("unit_id")
    private Long unitId;

    /**
     * 类目id
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * 属性id
     */
    @TableField("attribute")
    private String attribute;

    /**
     * 养护周期值
     */
    @TableField("curing_cycle")
    private Long curingCycle;

    /**
     * 养护周期类型（年|月|日）
     */
    @TableField("cycle_type")
    private Integer cycleType;

    /**
     * 生产类型（自制|委外|外采购）
     */
    @TableField("production_type")
    private Integer productionType;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getCuringCycle() {
        return curingCycle;
    }

    public void setCuringCycle(Long curingCycle) {
        this.curingCycle = curingCycle;
    }

    public Long getSpuClassificationId() {
        return spuClassificationId;
    }

    public void setSpuClassificationId(Long spuClassificationId) {
        this.spuClassificationId = spuClassificationId;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Integer shelfLife) {
        this.shelfLife = shelfLife;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public Date getProductionTime() {
        return productionTime;
    }

    public void setProductionTime(Date productionTime) {
        this.productionTime = productionTime;
    }

    public Integer getImportant() {
        return important;
    }

    public void setImportant(Integer important) {
        this.important = important;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getVulnerability() {
        return vulnerability;
    }

    public void setVulnerability(Integer vulnerability) {
        this.vulnerability = vulnerability;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Integer getCycleType() {
        return cycleType;
    }

    public void setCycleType(Integer cycleType) {
        this.cycleType = cycleType;
    }

    public Integer getProductionType() {
        return productionType;
    }

    public void setProductionType(Integer productionType) {
        this.productionType = productionType;
    }

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    @Override
    public String toString() {
        return "Spu{" +
                "spuId=" + spuId +
                ", name=" + name +
                ", shelfLife=" + shelfLife +
                ", inventory=" + inventory +
                ", productionTime=" + productionTime +
                ", important=" + important +
                ", weight=" + weight +
                ", materialId=" + materialId +
                ", cost=" + cost +
                ", vulnerability=" + vulnerability +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", updateTime=" + updateTime +
                ", updateUser=" + updateUser +
                ", display=" + display +
                ", deptId=" + deptId +
                ", classId=" + classId +
                ", unitId=" + unitId +
                ", categoryId=" + categoryId +
                ", attribute=" + attribute +
                "}";
    }
}
