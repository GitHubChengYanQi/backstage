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
 * 套餐分表
 * </p>
 *
 * @author qr
 * @since 2021-08-04
 */
@TableName("daoxin_erp_package_table")
public class ErpPackageTable implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

  /**
   * 套餐id
   */
  @TableField("package_id")
  private Long packageId;


    /**
     * 产品id
     */
    @TableField("item_id")
    private Long itemId;


  /**
   * 销售单价
   */
  @TableField("sale_price")
  private Long salePrice;

  /**
   * 总计
   */
  @TableField("total_price")
  private Long totalPrice;

  /**
   * 品牌
   */
  @TableField("brand_id")
  private Long brandId;

  /**
   * 数量
   */
  @TableField("quantity")
  private Long quantity;


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
  @TableField(value = "deptId", fill = FieldFill.INSERT)
  private Long deptId;



  @TableField(value = "tenant_id" , fill = FieldFill.INSERT)
  private Long tenantId;

  public Long getTenantId() {
    return tenantId;
  }

  public void setTenantId(Long tenantId) {
    this.tenantId = tenantId;
  }


  public Long getDeptId() {
    return deptId;
  }

  public void setDeptId(Long deptId) {
    this.deptId = deptId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPackageId() {
    return packageId;
  }

  public void setPackageId(Long packageId) {
    this.packageId = packageId;
  }

  public Long getItemId() {
    return itemId;
  }

  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }

  public Long getSalePrice() {
    return salePrice;
  }

  public void setSalePrice(Long salePrice) {
    this.salePrice = salePrice;
  }

  public Long getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(Long totalPrice) {
    this.totalPrice = totalPrice;
  }

  public Long getBrandId() {
    return brandId;
  }

  public void setBrandId(Long brandId) {
    this.brandId = brandId;
  }

  public Long getQuantity() {
    return quantity;
  }

  public void setQuantity(Long quantity) {
    this.quantity = quantity;
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


  @Override
  public String toString() {
    return "ErpPackageTable{" +
      "id=" + id +
      ", packageId=" + packageId +
      ", itemId=" + itemId +
      ", salePrice=" + salePrice +
      ", totalPrice=" + totalPrice +
      ", brandId=" + brandId +
      ", quantity=" + quantity +
      ", createUser=" + createUser +
      ", updateUser=" + updateUser +
      ", createTime=" + createTime +
      ", updateTime=" + updateTime +
      ", display=" + display +
      '}';
  }
}
