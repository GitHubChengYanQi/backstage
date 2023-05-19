package cn.atsoft.dasheng.storehouse.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
public class RestStorehouse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 纬度
     */
    @TableField("latitude")
    private BigDecimal latitude;

    @TableField("description")
    private String description;



     @TableField(value = "deptId",fill =FieldFill.INSERT_UPDATE)
    private Long deptId;

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
    @TableField("children")
    private String children;

    @TableField("childrens")
    private String childrens;

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
     * 名称
     */
    @TableField("sort")
    private Integer sort;

    @TableField("coding")
    private String coding;

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

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
    /**
     * 上级id
     */
    @TableField("pid")
    private Long pid;

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

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
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

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChildrens() {
        return childrens;
    }

    public void setChildrens(String childrens) {
        this.childrens = childrens;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "RestStorehouse{" +
                "latitude=" + latitude +
                ", deptId=" + deptId +
                ", children='" + children + '\'' +
                ", childrens='" + childrens + '\'' +
                ", longitude=" + longitude +
                ", storehouseId=" + storehouseId +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", coding='" + coding + '\'' +
                ", palce='" + palce + '\'' +
                ", measure=" + measure +
                ", capacity=" + capacity +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", updateTime=" + updateTime +
                ", updateUser=" + updateUser +
                ", display=" + display +
                ", pid=" + pid +
                '}';
    }
}
