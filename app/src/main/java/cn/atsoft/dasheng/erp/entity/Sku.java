package cn.atsoft.dasheng.erp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.models.auth.In;

import java.io.Serializable;

/**
 * <p>
 * sku表
 * </p>
 *
 * @author
 * @since 2021-10-18
 */
@TableName("goods_sku")
public class Sku implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 包装材料
     */
    @TableField("packaging")
    private String packaging;

    /**
     * 图幅
     */
    @TableField("view_frame")
    private String viewFrame;

    /**
     * 型号
     */
    @TableField("model")
    private String model;

    /**
     * 热处理
     */
    @TableField("heat_treatment")
    private String heatTreatment;
    /**
     * 级别
     */
    @TableField("level")
    private String level;
    /**
     * 表色
     */
    @TableField("color")
    private String color;
    /**
     * 尺寸
     */
    @TableField("sku_size")
    private String skuSize;
    /**
     * 重量
     */
    @TableField("weight")
    private String weight;
    /**
     * 材质id
     */
    @TableField("material_id")
    private String materialId;
    /**
     * 零件号
     */
    @TableField("part_no")
    private String partNo;
    /**
     * 国家标准
     */
    @TableField("national_standard")
    private String nationalStandard;
    /**
     * 之间方案id
     */
    @TableField("quality_plan_id")
    private Long qualityPlanId;

    @TableField(value = "file_id", fill = FieldFill.UPDATE)
    private String fileId;

    @TableField(value = "enclosure", fill = FieldFill.UPDATE)
    private String enclosure;

    @TableField(value = "images", fill = FieldFill.UPDATE)
    private String images;

    @TableField(value = "drawing", fill = FieldFill.UPDATE)
    private String drawing;
    /**
     * 批量
     */
    @TableField("batch")
    private Integer batch;

    public Integer getBatch() {
        return batch;
    }

    public void setBatch(Integer batch) {
        this.batch = batch;
    }

    /**
     * 类型
     */
    @TableField("type")
    private Integer type;

    @TableId(value = "sku_id", type = IdType.ID_WORKER)
    private Long skuId;

    /**
     * sku名字
     */
    @TableField("sku_name")
    private String skuName;
    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;
    /**
     * 编码
     */
    @TableField("coding")
    private String coding;

    @TableField("is_ban")
    private Integer isBan;


    @TableField("add_method")
    private Integer addMethod;
    @TableField("standard")
    private String standard;

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public Integer getIsBan() {
        return isBan;
    }

    @TableField("specifications")
    public String specifications;

    public String getSpecifications() {
        return specifications;
    }

    public String getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(String enclosure) {
        this.enclosure = enclosure;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public void setIsBan(Integer isBan) {
        this.isBan = isBan;
    }

    @TableField("sku_value")
    private String skuValue;

    @TableField("sku_value_md5")
    private String skuValueMd5;


    public String getSkuValueMd5() {
        return skuValueMd5;
    }

    public void setSkuValueMd5(String skuValueMd5) {
        this.skuValueMd5 = skuValueMd5;
    }

    public String getSkuValue() {
        return skuValue;
    }

    public void setSkuValue(String skuValue) {
        this.skuValue = skuValue;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getQualityPlanId() {
        return qualityPlanId;
    }

    public void setQualityPlanId(Long qualityPlanId) {
        this.qualityPlanId = qualityPlanId;
    }

    public Integer getAddMethod() {
        return addMethod;
    }

    public void setAddMethod(Integer addMethod) {
        this.addMethod = addMethod;
    }

    /**
     * spu id
     */
    @TableField("spu_id")
    private Long spuId;
    /**
     * 养护周期(天)
     */
    @TableField("maintenance_period")
    private Integer maintenancePeriod;

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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    /**
     * 状态
     */
    @TableField("display")
    private Integer display;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSkuSize() {
        return skuSize;
    }

    public void setSkuSize(String skuSize) {
        this.skuSize = skuSize;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getNationalStandard() {
        return nationalStandard;
    }

    public void setNationalStandard(String nationalStandard) {
        this.nationalStandard = nationalStandard;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getDrawing() {
        return drawing;
    }

    public void setDrawing(String drawing) {
        this.drawing = drawing;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
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

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Integer getMaintenancePeriod() {
        return maintenancePeriod;
    }

    public void setMaintenancePeriod(Integer maintenancePeriod) {
        this.maintenancePeriod = maintenancePeriod;
    }

    public String getHeatTreatment() {
        return heatTreatment;
    }

    public void setHeatTreatment(String heatTreatment) {
        this.heatTreatment = heatTreatment;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public String getViewFrame() {
        return viewFrame;
    }

    public void setViewFrame(String viewFrame) {
        this.viewFrame = viewFrame;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "Sku{" +
                "skuId=" + skuId +
                ", skuName=" + skuName +
                ", spuId=" + spuId +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", updateTime=" + updateTime +
                ", updateUser=" + updateUser +
                ", display=" + display +
                "}";
    }
}
