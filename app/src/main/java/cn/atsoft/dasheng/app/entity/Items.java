package cn.atsoft.dasheng.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 物品表
 * </p>
 *
 * @author 1
 * @since 2021-07-14
 */
@TableName("daoxin_erp_items")
public class Items implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 物品Id
     */
      @TableId(value = "item_id", type = IdType.ID_WORKER)
    private Long itemId;

    /**
     * 物品名字
     */
    @TableField("name")
    private String name;

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
    private Float important;

    /**
     * 物品重量
     */
    @TableField("weight")
    private Double weight;

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
     * 状态
     */
    @TableField("display")
    private Integer display;

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


    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
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

    public Float getImportant() {
        return important;
    }

    public void setImportant(Float important) {
        this.important = important;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
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

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
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

    @Override
    public String toString() {
        return "Items{" +
        "itemId=" + itemId +
        ", name=" + name +
        ", shelfLife=" + shelfLife +
        ", inventory=" + inventory +
        ", productionTime=" + productionTime +
        ", important=" + important +
        ", weight=" + weight +
        ", materialId=" + materialId +
        ", cost=" + cost +
        ", vulnerability=" + vulnerability +
        ", display=" + display +
        ", createTime=" + createTime +
        ", createUser=" + createUser +
        ", updateTime=" + updateTime +
        ", updateUser=" + updateUser +
        "}";
    }
}
