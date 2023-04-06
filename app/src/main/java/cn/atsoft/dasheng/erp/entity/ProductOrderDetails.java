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
 * 产品订单详情
 * </p>
 *
 * @author song
 * @since 2021-10-20
 */
@TableName("daoxin_erp_product_order_details")
public class ProductOrderDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品订单详情id
     */
      @TableId(value = "product_order_details_id", type = IdType.ID_WORKER)
    private Long productOrderDetailsId;

    /**
     * 产品订单id
     */
    @TableField("product_order_id")
    private Long productOrderId;

    /**
     * spuId
     */
    @TableField("spu_id")
    private Long spuId;

    /**
     * skuId
     */
    @TableField("sku")
    private String sku;

    /**
     * 数量
     */
    @TableField("number")
    private Long number;

    /**
     * 金额
     */
    @TableField("money")
    private Integer money;

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
    @TableField(value = "deptId" , fill = FieldFill.INSERT)
    private Long deptId;


    public Long getProductOrderDetailsId() {
        return productOrderDetailsId;
    }

    public void setProductOrderDetailsId(Long productOrderDetailsId) {
        this.productOrderDetailsId = productOrderDetailsId;
    }

    public Long getProductOrderId() {
        return productOrderId;
    }

    public void setProductOrderId(Long productOrderId) {
        this.productOrderId = productOrderId;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
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

    @Override
    public String toString() {
        return "ProductOrderDetails{" +
        "productOrderDetailsId=" + productOrderDetailsId +
        ", productOrderId=" + productOrderId +
        ", spuId=" + spuId +
        ", sku=" + sku +
        ", number=" + number +
        ", money=" + money +
        ", createTime=" + createTime +
        ", createUser=" + createUser +
        ", updateTime=" + updateTime +
        ", updateUser=" + updateUser +
        ", display=" + display +
        ", deptId=" + deptId +
        "}";
    }
}
