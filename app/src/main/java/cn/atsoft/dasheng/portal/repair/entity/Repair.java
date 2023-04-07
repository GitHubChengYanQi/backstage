package cn.atsoft.dasheng.portal.repair.entity;

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
 * 报修
 * </p>
 *
 * @author siqiang
 * @since 2021-08-20
 */
@TableName("daoxin_portal_repair")
public class Repair implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 报修id
     */
    @TableId(value = "repair_id", type = IdType.ID_WORKER)
    private Long repairId;
    @TableField("wx_area")
    private String wxArea;

    /**
     * 报修单位
     */
    @TableField("company_id")
    private Long companyId;

    @TableField("dynamic_id")
    private Long dynamic;


    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    @TableField("number")
    private Long number;

    /**
     * 保修部位图片
     */
    @TableField("item_img_url")
    private String itemImgUrl;

    /**
     * 设备id
     */
    @TableField("item_id")
    private Long itemId;

    /**
     * 服务类型
     */
    @TableField("service_type")
    private String serviceType;

    /**
     * 期望到达日期
     */
    @TableField("expect_time")
    private Date expectTime;

    /**
     * 描述
     */
    @TableField("comment")
    private String comment;
    @TableField("name")
    private Long name;

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
     * 工程进度
     */
    @TableField("progress")
    private Long progress;

    /**
     * 维修费用
     */
    @TableField("money")
    private Long money;

    /**
     * 质保类型
     */
    @TableField("quality_type")
    private String qualityType;

    /**
     * 合同类型
     */
    @TableField("contract_type")
    private String contractType;

    /**
     * 客户id
     */
    @TableField("customer_id")
    private Long customerId;


    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * 省
     */
    @TableField("province")
    private String province;

    /**
     * 市
     */
    @TableField("city")
    private String city;

    /**
     * 区
     */
    @TableField("area")
    private String area;

    /**
     * 详细地址
     */
    @TableField("address")
    private String address;

    /**
     * 姓名
     */
    @TableField("people")
    private String people;

    /**
     * 职务
     */
    @TableField("position")
    private String position;

    /**
     * 电话
     */
    @TableField("telephone")
    private Long telephone;

    /**
     * 权限
     */
    @TableField("power")
    private int power;
    /**
     * 部门id
     */
    @TableField(value = "deptId",fill =FieldFill.INSERT)
    private Long deptId;
    @TableField(value = "tenant_id" , fill = FieldFill.INSERT)
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Long getTelephone() {
        return telephone;
    }

    public void setTelephone(Long telephone) {
        this.telephone = telephone;
    }

    public Long getRepairId() {
        return repairId;
    }

    public void setRepairId(Long repairId) {
        this.repairId = repairId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getItemImgUrl() {
        return itemImgUrl;
    }

    public void setItemImgUrl(String itemImgUrl) {
        this.itemImgUrl = itemImgUrl;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Date getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(Date expectTime) {
        this.expectTime = expectTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public Long getProgress() {
        return progress;
    }

    public void setProgress(Long progress) {
        this.progress = progress;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public String getQualityType() {
        return qualityType;
    }

    public void setQualityType(String qualityType) {
        this.qualityType = qualityType;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public Long getDynamic() {
        return dynamic;
    }

    public void setDynamic(Long dynamic) {
        this.dynamic = dynamic;
    }

    public String getWxArea() {
        return wxArea;
    }

    public void setWxArea(String wxArea) {
        this.wxArea = wxArea;
    }

    public Long getName() {
        return name;
    }

    public void setName(Long name) {
        this.name = name;
    }


    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    @Override
    public String toString() {
        return "Repair{" +
                "repairId=" + repairId +
                ", companyId=" + companyId +
                ", itemImgUrl=" + itemImgUrl +
                ", itemId=" + itemId +
                ", serviceType=" + serviceType +
                ", expectTime=" + expectTime +
                ", comment=" + comment +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", updateTime=" + updateTime +
                ", updateUser=" + updateUser +
                ", display=" + display +
                ", progress=" + progress +
                ", money=" + money +
                ", qualityType=" + qualityType +
                ", contractType=" + contractType +
                "}";
    }
}
