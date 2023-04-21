package cn.atsoft.dasheng.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * <p>
 * 出库单
 * </p>
 *
 * @author cheng
 * @since 2021-08-16
 */
@TableName("daoxin_erp_outstock_order")
public class OutstockOrder implements Serializable {

    private static final long serialVersionUID = 1L;


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
     * 出库详细id
     */
    @TableId(value = "outstock_order_id", type = IdType.ID_WORKER)
    private Long outstockOrderId;


    @TableField("storehouse_id")
    private Long storehouseId;

    /**
     * 发货申请Id
     */
    @TableField("outstock_apply_id")
    private Long outstockApplyId;
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



    /**
     * 出库状态
     */
    @TableField("state")
    private Integer state;
    /**
     * 经手人
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 备注
     */
    @TableField("note")
    private String note;


    @TableField("coding")
    private String coding;
    /**
     * 计划出库时间
     */
    @TableField("time")
    private Date time;

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
     * 主题
     */
    @TableField("theme")
    private String theme;

    /**
     * 来源
     */
    @TableField("origin")
    private String origin;

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

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    public Long getStorehouseId() {
        return storehouseId;
    }

    public void setStorehouseId(Long storehouseId) {
        this.storehouseId = storehouseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getOutstockOrderId() {
        return outstockOrderId;
    }

    public void setOutstockOrderId(Long outstockOrderId) {
        this.outstockOrderId = outstockOrderId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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

    public Long getOutstockApplyId() {
        return outstockApplyId;
    }

    public void setOutstockApplyId(Long outstockApplyId) {
        this.outstockApplyId = outstockApplyId;
    }

    @Override
    public String toString() {
        return "OutstockOrder{" +
                "outstockOrderId=" + outstockOrderId +
                ", state=" + state +
                ", time=" + time +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", display=" + display +
                "}";
    }
}
