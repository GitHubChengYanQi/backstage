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
 * 生产卡片
 * </p>
 *
 * @author 
 * @since 2022-02-28
 */
@TableName("daoxin_production_card")
public class ProductionCard implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 生产卡片id
     */
      @TableId(value = "production_card_id", type = IdType.ID_WORKER)
    private Long productionCardId;

    /**
     * 工单id
     */
    @TableField("work_order_id")
    private Long workOrderId;

    /**
     * 物料id
     */
    @TableField("sku_id")
    private Long skuId;

    /**
     * 创建者
     */
      @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 部门编号
     */
@TableField(value = "deptId" , fill = FieldFill.INSERT)
    private Long deptId;
    /**
     * 租户编号
     */
    @TableField(value = "tenant_id" , fill = FieldFill.INSERT)
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * 状态
     */
    @TableField("display")
    private Integer display;

    /**
     * 状态
     */
    @TableField("source")
    private String source;
    /**
     * 卡片编码
     */
    @TableField("card_coding")
    private String cardCoding;
    /**
     * 状态
     */
    @TableField("source_id")
    private Long sourceId;
    /**
     * 状态
     */
    @TableField("origin")
    private String origin;


    /**
     * 状态
     */
    @TableField("theme")
    private String theme;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

    /**
     * BOM数量
     */
    @TableField("bom_count")
    private Integer bomCount;

    /**
     * 修改时间
     */
      @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 创建时间
     */
      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改者
     */
      @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;


    public Long getProductionCardId() {
        return productionCardId;
    }

    public void setProductionCardId(Long productionCardId) {
        this.productionCardId = productionCardId;
    }

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getCardCoding() {
        return cardCoding;
    }

    public void setCardCoding(String cardCoding) {
        this.cardCoding = cardCoding;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBomCount() {
        return bomCount;
    }

    public void setBomCount(Integer bomCount) {
        this.bomCount = bomCount;
    }

    @Override
    public String toString() {
        return "ProductionCard{" +
                "productionCardId=" + productionCardId +
                ", workOrderId=" + workOrderId +
                ", skuId=" + skuId +
                ", createUser=" + createUser +
                ", deptId=" + deptId +
                ", display=" + display +
                ", source='" + source + '\'' +
                ", cardCoding='" + cardCoding + '\'' +
                ", sourceId=" + sourceId +
                ", origin='" + origin + '\'' +
                ", theme='" + theme + '\'' +
                ", status=" + status +
                ", bomCount=" + bomCount +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                ", updateUser=" + updateUser +
                '}';
    }
}
