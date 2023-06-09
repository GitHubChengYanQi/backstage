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
 * 工单
 * </p>
 *
 * @author 
 * @since 2022-02-28
 */
@TableName("daoxin_production_work_order")
public class ProductionWorkOrder implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "work_order_id", type = IdType.ID_WORKER)
    private Long workOrderId;

    /**
     * 工序id
     */
    @TableField("ship_setp_id")
    private Long shipSetpId;

    public Long getCardSkuId() {
        return cardSkuId;
    }

    public void setCardSkuId(Long cardSkuId) {
        this.cardSkuId = cardSkuId;
    }

    /**
     * 工序id
     */
    @TableField("card_sku_id")
    private Long cardSkuId;
    /**
     * 生产卡片id
     */
    @TableField("card_id")
    private Long cardId;


    @TableField("sku_id")
    private Long skuId;

    /**
     * 生产数量
     */
    @TableField("count")
    private Integer count;

    /**
     * 投入物料
     */
    @TableField("in_sku_id")
    private Long inSkuId;

    /**
     * 产出物料
     */
    @TableField("out_sku_id")
    private Long outSkuId;

    public Long getStepsId() {
        return stepsId;
    }

    public void setStepsId(Long stepsId) {
        this.stepsId = stepsId;
    }

    /**
     * 步骤id
     */
    @TableField("steps_id")
    private Long stepsId;



    /**
     * 投入物料
     */
    @TableField("in_sku_number")
    private Integer inSkuNumber;

    public Long getInSkuId() {
        return inSkuId;
    }

    public void setInSkuId(Long inSkuId) {
        this.inSkuId = inSkuId;
    }

    public Long getOutSkuId() {
        return outSkuId;
    }

    public void setOutSkuId(Long outSkuId) {
        this.outSkuId = outSkuId;
    }

    public Integer getInSkuNumber() {
        return inSkuNumber;
    }

    public void setInSkuNumber(Integer inSkuNumber) {
        this.inSkuNumber = inSkuNumber;
    }

    public Integer getOutSkuNumber() {
        return outSkuNumber;
    }

    public void setOutSkuNumber(Integer outSkuNumber) {
        this.outSkuNumber = outSkuNumber;
    }

    /**
     * 产出物料
     */
    @TableField("out_sku_number")
    private Integer outSkuNumber;

    /**
     * 来源
     */
    @TableField("source")
    private String source;

    /**
     * 来源id
     */
    @TableField("source_id")
    private Long sourceId;

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

    /**
     * 主题
     */
    @TableField("theme")
    private String theme;

    /**
     * 来源Json字符串
     */
    @TableField("origin")
    private String origin;



    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Long getShipSetpId() {
        return shipSetpId;
    }

    public void setShipSetpId(Long shipSetpId) {
        this.shipSetpId = shipSetpId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        return "ProductionWorkOrder{" +
        "workOrderId=" + workOrderId +
        ", shipSetpId=" + shipSetpId +
        ", skuId=" + skuId +
        ", count=" + count +
        ", source=" + source +
        ", sourceId=" + sourceId +
        ", createUser=" + createUser +
        ", deptId=" + deptId +
        ", display=" + display +
        ", updateTime=" + updateTime +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", theme=" + theme +
        ", origin=" + origin +
        "}";
    }
}
