package cn.atsoft.dasheng.crm.entity.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import com.baomidou.mybatisplus.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 客户地址表
 * </p>
 *
 * @author
 * @since 2021-07-23
 */
@TableName("daoxin_crm_adress")
public class AdressExcelItem implements Serializable {


    @NotNull(message = "客户不能为空")
    @Excel(name = "客户名称")
    private String customerName;
    @NotNull(message = "地区不能为空")
    @Excel(name = "地区名称")
    private String regionName;

    /**
     * 地址id
     */
    @TableId(value = "adress_id", type = IdType.ID_WORKER)
    @Excel(name = "地址id")
    private Long adressId;

    /**
     * 地址
     */
    @TableField("location")
    @Excel(name = "地址")
    private String location;

    /**
     * 地区
     */
    @TableField("region")
    @Excel(name = "地区id")
    private String region;


    /**
     * 经度
     */
    @Excel(name = "经度")
    @TableField("longitude")
    private Double longitude;

    /**
     * 纬度
     */
    @Excel(name = "纬度")
    @TableField("latitude")
    private Double latitude;

    /**
     * 创建者
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    @Excel(name = "创建者")
    private Long createUser;

    /**
     * 修改者
     */
    @Excel(name = "修改者")
    @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @Excel(name = "修改时间")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 状态
     */
    @Excel(name = "状态")
    @TableField("display")
    private Integer display;

    /**
     * 部门id
     */
    @Excel(name = "部门id")
    @TableField("deptId")
    private Long deptId;

    /**
     * 客户id
     */
    @Excel(name = "客户id")
    @TableField("customer_id")
    private Long customerId;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Long getAdressId() {
        return adressId;
    }

    public void setAdressId(Long adressId) {
        this.adressId = adressId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    @Override
    public String toString() {
        return "Adress{" +
                "adressId=" + adressId +
                ", location='" + location + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", display=" + display +
                ", deptId=" + deptId +
                ", customerId=" + customerId +
                '}';
    }
}
