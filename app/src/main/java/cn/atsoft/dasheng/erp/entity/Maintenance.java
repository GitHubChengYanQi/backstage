package cn.atsoft.dasheng.erp.entity;

import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 养护申请主表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-06-28
 */
@TableName("daoxin_erp_maintenance")
public class Maintenance implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 养护
     */
      @TableId(value = "maintenance_id", type = IdType.ID_WORKER)
    private Long maintenanceId;

    /**
     * name
     */
    @TableField("maintenance_name")
    private String maintenanceName;
    /**
    /**
     * coding
     */
    @TableField("coding")
    private String coding;
    /**
     * 材质id
     */
    @TableField("material_ids")
    private String  materialIds;


    /**
     * 品牌id
     */
    @TableField("brand_ids")
    private String brandIds;

    /**
     * 库位id
     */
    @TableField("storehouse_positions_ids")
    private String storehousePositionsIds;
    /**
     * 库位id
     */
    @TableField("parts_ids")
    private String partsIds;

    /**
     * 库位id
     */
    @TableField("spu_classification_ids")
    private String spuClassificationIds;

    /**
     * 负责人
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 类型
     */
    @TableField("type")
    private String type;

    /**
     * 养护临近期
     */
    @TableField("near_maintenance")
    private Integer nearMaintenance;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 创建者
     */
      @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

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

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 附件
     */
    @TableField("enclosure")
    private String enclosure;

    /**
     * 备注
     */
    @TableField("note")
    private String note;


    public Long getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(Long maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public String getMaintenanceName() {
        return maintenanceName;
    }

    public void setMaintenanceName(String maintenanceName) {
        this.maintenanceName = maintenanceName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNearMaintenance() {
        return nearMaintenance;
    }

    public void setNearMaintenance(Integer nearMaintenance) {
        this.nearMaintenance = nearMaintenance;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(String enclosure) {
        this.enclosure = enclosure;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMaterialIds() {
        return materialIds;
    }

    public void setMaterialIds(String materialIds) {
        this.materialIds = materialIds;
    }

    public String getBrandIds() {
        return brandIds;
    }

    public void setBrandIds(String brandIds) {
        this.brandIds = brandIds;
    }

    public String getStorehousePositionsIds() {
        return storehousePositionsIds;
    }

    public void setStorehousePositionsIds(String storehousePositionsIds) {
        this.storehousePositionsIds = storehousePositionsIds;
    }

    public String getPartsIds() {
        return partsIds;
    }

    public void setPartsIds(String partsIds) {
        this.partsIds = partsIds;
    }

    public String getSpuClassificationIds() {
        return spuClassificationIds;
    }

    public void setSpuClassificationIds(String spuClassificationIds) {
        this.spuClassificationIds = spuClassificationIds;
    }

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    @Override
    public String toString() {
        return "Maintenance{" +
        "maintenanceId=" + maintenanceId +
        ", maintenanceName=" + maintenanceName +
        ", userId=" + userId +
        ", type=" + type +
        ", nearMaintenance=" + nearMaintenance +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", display=" + display +
        ", deptId=" + deptId +
        ", status=" + status +
        ", enclosure=" + enclosure +
        ", note=" + note +
        "}";
    }
}
