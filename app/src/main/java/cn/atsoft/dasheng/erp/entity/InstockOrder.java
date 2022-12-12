package cn.atsoft.dasheng.erp.entity;

import cn.atsoft.dasheng.crm.entity.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 入库单
 * </p>
 *
 * @author song
 * @since 2021-10-06
 */
@TableName("daoxin_erp_instock_order")
public class InstockOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("task_id")
    private Long taskId;

    @TableField("push_people")
    private String pushPeople;
    /**
     * 注意事项
     */
    @TableField("notice_id")
    private String noticeId;
    /**
     * 来源
     */
    @TableField("source")
    private String source;

    /**
     * 客户id
     */
    @TableField("customer_id")
    private Long customerId;

    /**
     * 类型
     */
    @TableField("type")
    private String type;

    /**
     * 来源id
     */
    @TableField("source_id")
    private Long sourceId;

    /**
     * 库位id
     */
    @TableField("storehouse_positions_id")
    private Long storehousePositionsId;

    @TableField("register_time")
    private Date registerTime;

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    @TableField("coding")
    private String coding;
    /**
     * 入库单
     */
    @TableId(value = "instock_order_id", type = IdType.ID_WORKER)
    private Long instockOrderId;
    /**
     * 仓库id
     */
    @TableField("storehouse_id")
    private Long storeHouseId;

    /**
     * 库管人员负责人
     */
    @TableField("stock_user_id")
    private Long stockUserId;

    /**
     * 入库时间
     */
    @TableField("instock_time")
    private Date instockTime;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 是否加急
     */
    @TableField("urgent")
    private Integer urgent;


    /**
     * 附件（最多5个，逗号分隔）
     */
    @TableField("enclosure")
    private String enclosure;

    /**
     * 状态
     */
    @TableField("status")
    private Long status;

    /**
     * 负责人
     */
    @TableField("user_id")
    private Long userId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

    @TableField("display")
    private Integer display;

    /**
     * 部门编号
     */
    @TableField(value = "deptId", fill = FieldFill.INSERT)
    private Long deptId;

    /**
     * 入库状态
     */
    @TableField("state")
    private Integer state;

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


    /**
     * 入库类型
     */
    @TableField("instock_type")
    private String instockType;

    public String getInstockType() {
        return instockType;
    }

    public void setInstockType(String instockType) {
        this.instockType = instockType;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Date getInstockTime() {
        return instockTime;
    }

    public void setInstockTime(Date instockTime) {
        this.instockTime = instockTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getStockUserId() {
        return stockUserId;
    }

    public void setStockUserId(Long stockUserId) {
        this.stockUserId = stockUserId;
    }

    public Integer getUrgent() {
        return urgent;
    }

    public void setUrgent(Integer urgent) {
        this.urgent = urgent;
    }

    public String getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(String enclosure) {
        this.enclosure = enclosure;
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

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Long getStoreHouseId() {
        return storeHouseId;
    }

    public void setStoreHouseId(Long storeHouseId) {
        this.storeHouseId = storeHouseId;
    }

    public Long getInstockOrderId() {
        return instockOrderId;
    }

    public void setInstockOrderId(Long instockOrderId) {
        this.instockOrderId = instockOrderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getStorehousePositionsId() {
        return storehousePositionsId;
    }

    public void setStorehousePositionsId(Long storehousePositionsId) {
        this.storehousePositionsId = storehousePositionsId;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getPushPeople() {
        return pushPeople;
    }

    public void setPushPeople(String pushPeople) {
        this.pushPeople = pushPeople;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "InstockOrder{" +
                "taskId=" + taskId +
                ", pushPeople='" + pushPeople + '\'' +
                ", noticeId='" + noticeId + '\'' +
                ", source='" + source + '\'' +
                ", customerId=" + customerId +
                ", type='" + type + '\'' +
                ", sourceId=" + sourceId +
                ", storehousePositionsId=" + storehousePositionsId +
                ", registerTime=" + registerTime +
                ", coding='" + coding + '\'' +
                ", instockOrderId=" + instockOrderId +
                ", storeHouseId=" + storeHouseId +
                ", stockUserId=" + stockUserId +
                ", instockTime=" + instockTime +
                ", remark='" + remark + '\'' +
                ", urgent=" + urgent +
                ", enclosure='" + enclosure + '\'' +
                ", status=" + status +
                ", userId=" + userId +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", updateTime=" + updateTime +
                ", updateUser=" + updateUser +
                ", display=" + display +
                ", deptId=" + deptId +
                ", state=" + state +
                ", theme='" + theme + '\'' +
                ", origin='" + origin + '\'' +
                '}';
    }
}
