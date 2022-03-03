package cn.atsoft.dasheng.form.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * <p>
 * 工序步骤设置表
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@TableName("activiti_setp_set")
public class ActivitiSetpSet implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @TableId(value = "set_id", type = IdType.AUTO)
    private Long setId;
    /**
     * 类型
     */
    @TableField("production_type")
    private String productionType;

    /**
     * 工位Id
     */
    @TableField("production_station_id")
    private Long productionStationId;
    /**
     * 步骤Id
     */
    @TableField("setps_id")
    private Long setpsId;

    /**
     * 工序Id
     */
    @TableField("ship_setp_id")
    private Long shipSetpId;

    /**
     * 标准工作时长，按小时计算
     */
    @TableField("length")
    private Integer length;

    /**
     * 删除状态
     */
    @TableField("display")
    private Integer display;

    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;


    public Long getSetId() {
        return setId;
    }

    public void setSetId(Long setId) {
        this.setId = setId;
    }

    public Long getSetpsId() {
        return setpsId;
    }

    public void setSetpsId(Long setpsId) {
        this.setpsId = setpsId;
    }

    public Long getShipSetpId() {
        return shipSetpId;
    }

    public void setShipSetpId(Long shipSetpId) {
        this.shipSetpId = shipSetpId;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
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

    public String getProductionType() {
        return productionType;
    }

    public void setProductionType(String productionType) {
        this.productionType = productionType;
    }

    public Long getProductionStationId() {
        return productionStationId;
    }

    public void setProductionStationId(Long productionStationId) {
        this.productionStationId = productionStationId;
    }

    @Override
    public String toString() {
        return "ActivitiSetpSet{" +
                "setId=" + setId +
                ", setpsId=" + setpsId +
                ", shipSetpId=" + shipSetpId +
                ", length=" + length +
                ", display=" + display +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
