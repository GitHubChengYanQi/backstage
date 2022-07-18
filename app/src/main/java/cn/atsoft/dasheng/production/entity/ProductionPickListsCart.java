package cn.atsoft.dasheng.production.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 领料单详情表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-25
 */
@TableName("daoxin_production_pick_lists_cart")
public class ProductionPickListsCart implements Serializable {

    private static final long serialVersionUID = 1L;

    public Long getInkindId() {
        return inkindId;
    }

    public void setInkindId(Long inkindId) {
        this.inkindId = inkindId;
    }

    @TableField("inkind_id")
    private Long inkindId;
    /**
     * 子表id
     */
    @TableId(value = "pick_lists_cart", type = IdType.ID_WORKER)
    private Long pickListsCart;



    @TableField("storehouse_positions_id")
    private Long storehousePositionsId;




    @TableField("type")
    private String type;


    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

    @TableField("pick_lists_detail_id")
    private Long pickListsDetailId;




    @TableField("storehouse_id")
    private Long storehouseId;

    @TableField("brand_id")
    private Long brandId;
    @TableField("customer_id")
    private Long customerId;


    /**
     * 物料id
     */
    @TableField("sku_id")
    private Long skuId;
    /**
     * 主表id
     */
    @TableField("pick_lists_id")
    private Long pickListsId;


    @TableField("number")
    private Integer number;

    /**
     * 创建者
     */
      @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改者
     */
      @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

    /**
     * 创建时间
     */
      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
      @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 状态
     */
    @TableField("display")
    private Integer display;

    /**
     * 部门id
     */
    @TableField("deptId")
    private Long deptId;


    public Long getStorehousePositionsId() {
        return storehousePositionsId;
    }

    public void setStorehousePositionsId(Long storehousePositionsId) {
        this.storehousePositionsId = storehousePositionsId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getStorehouseId() {
        return storehouseId;
    }

    public Long getPickListsDetailId() {
        return pickListsDetailId;
    }

    public void setPickListsDetailId(Long pickListsDetailId) {
        this.pickListsDetailId = pickListsDetailId;
    }

    public void setStorehouseId(Long storehouseId) {
        this.storehouseId = storehouseId;
    }

    public Long getPickListsId() {
        return pickListsId;
    }

    public void setPickListsId(Long pickListsId) {
        this.pickListsId = pickListsId;
    }

    public Long getPickListsCart() {
        return pickListsCart;
    }

    public void setPickListsCart(Long pickListsCart) {
        this.pickListsCart = pickListsCart;
    }



    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ProductionPickListsCart{" +
        "pickListsCart=" + pickListsCart +
        ", skuId=" + skuId +
        ", number=" + number +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", display=" + display +
        ", deptId=" + deptId +
        "}";
    }
}
