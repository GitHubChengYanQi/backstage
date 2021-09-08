package cn.atsoft.dasheng.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 地点表
 * </p>
 *
 * @author
 * @since 2021-07-15
 */
@Data
@TableName("daoxin_erp_storehouse")
public class Storehouse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 纬度
     */
    @TableField("latitude")
    private BigDecimal latitude;


    /**
     * 经度
     */
    @TableField("longitude")
    private BigDecimal longitude;

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    /**
     * 仓库id
     */
    @TableId(value = "storehouse_id", type = IdType.ID_WORKER)
    private Long storehouseId;

    /**
     * 名称
     */
    @TableField("name")
    private String name;


    /**
     * 位置
     */
    @TableField("palce")
    private String palce;

    /**
     * 面积
     */
    @TableField("measure")
    private Long measure;

    /**
     * 容量
     */
    @TableField("capacity")
    private Long capacity;

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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getStorehouseId() {
        return storehouseId;
    }

    public void setStorehouseId(Long storehouseId) {
        this.storehouseId = storehouseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPalce() {
        return palce;
    }

    public void setPalce(String palce) {
        this.palce = palce;
    }

    public Long getMeasure() {
        return measure;
    }

    public void setMeasure(Long measure) {
        this.measure = measure;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
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

    @Override
    public String toString() {
        return "Place{" +
                "palceId=" + storehouseId +
                ", name=" + name +
                ", palce=" + palce +
                ", measure=" + measure +
                ", capacity=" + capacity +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", updateTime=" + updateTime +
                ", updateUser=" + updateUser +
                ", display=" + display +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                "}";
    }
}
