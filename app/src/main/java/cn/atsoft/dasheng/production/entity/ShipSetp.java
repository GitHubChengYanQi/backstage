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
 * 工序表
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
@TableName("daoxin_ship_setp")
public class ShipSetp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工序
     */
    @TableId(value = "ship_setp_id", type = IdType.ID_WORKER)
    private Long shipSetpId;

    /**
     * 工序名称
     */
    @TableField("ship_setp_name")
    private String shipSetpName;

    /**
     * 工序分类
     */
    @TableField("ship_setp_class_id")
    private Long shipSetpClassId;

    /**
     * 编码
     */
    @TableField("coding")
    private String coding;

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 人员id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 工位id
     */
    @TableField("production_station_id")
    private Long productionStationId;

    /**
     * 附件
     */
    @TableField("accessories")
    private String accessories;

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


    public Long getShipSetpId() {
        return shipSetpId;
    }

    public void setShipSetpId(Long shipSetpId) {
        this.shipSetpId = shipSetpId;
    }

    public String getShipSetpName() {
        return shipSetpName;
    }

    public void setShipSetpName(String shipSetpName) {
        this.shipSetpName = shipSetpName;
    }

    public Long getShipSetpClassId() {
        return shipSetpClassId;
    }

    public void setShipSetpClassId(Long shipSetpClass) {
        this.shipSetpClassId = shipSetpClass;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductionStationId() {
        return productionStationId;
    }

    public void setProductionStationId(Long productionStationId) {
        this.productionStationId = productionStationId;
    }

    public String getAccessories() {
        return accessories;
    }

    public void setAccessories(String accessories) {
        this.accessories = accessories;
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

    @Override
    public String toString() {
        return "DaoxinShipSetp{" +
                "shipSetpId=" + shipSetpId +
                ", shipSetpName=" + shipSetpName +
                ", shipSetpClass=" + shipSetpClassId +
                ", remark=" + remark +
                ", userId=" + userId +
                ", productionStationId=" + productionStationId +
                ", accessories=" + accessories +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", display=" + display +
                ", deptId=" + deptId +
                "}";
    }
}
