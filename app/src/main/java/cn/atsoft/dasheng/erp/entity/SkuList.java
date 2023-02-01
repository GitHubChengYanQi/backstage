package cn.atsoft.dasheng.erp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("view_sku")
public class SkuList implements Serializable {
    private static final long serialVersionUID = 1L;

    //物料Id
    @TableId(value = "sku_id", type = IdType.ID_WORKER)
    private Long skuId;
    //成品码
    @TableField("standard")
    private String standard;
    //物料名字
    @TableField("sku_name")
    private String skuName;
    //物料价格
    @TableField("price")
    private double price;

    public double getPrice() {
        return price/100;
    }

    //品牌名称
    @TableField("brand_name")
    private String brandName;

    //供应商名称
    @TableField("customer_name")
    private String customerName;

    @TableField("sku_value")
    private String skuValue;

    @TableField("spu_id")
    private Long spuId;

    @TableField("spu_name")
    private String spuName;



    @TableField("category_id")
    private Long categoryId;

    @TableField("category_name")
    private String categoryName;

    @TableField("material_id")
    private String materialId;

    @TableField("material_name")
    private String materialName;

    //bom数
    @TableField("bom_num")
    private Integer bomNum;

    //工艺数
    @TableField("ship_Num")
    private Integer shipNum;

    @TableField("stock_num")
    private Integer stockNum;

    //单位
    @TableField("unit_id")
    private Long unitId;

    //单位名称
    @TableField("unit_name")
    private String unitName;

    //库存预警最小值
    @TableField("stock_warning_min")
    private Integer stockWarningMin;

    //库存预警最大值
    @TableField("stock_warning_max")
    private Integer stockWarningMax;

    @TableField("remarks")
    private String remarks;

    @TableField("coding")
    private String coding;

    @TableField("model_coding")
    private String modelCoding;

    @TableField("view_frame")
    private String viewFrame;

    @TableField("model")
    private String model;

    @TableField("packaging")
    private String packaging;

    @TableField("heat_treatment")
    private String heatTreatment;

    @TableField("level")
    private String level;

    @TableField("sku_size")
    private String skuSize;

    @TableField("color")
    private String color;

    @TableField("weight")
    private String weight;


    @TableField("part_no")
    private String partNo;

    @TableField("national_standard")
    private String nationalStandard;

    @TableField("maintenance_period")
    private Integer maintenancePeriod;

    @TableField("add_method")
    private Integer addMethod;

    @TableField("enclosure")
    private String enclosure;

    @TableField("drawing")
    private String drawing;

    @TableField("images")
    private String images;



    @TableField("file_id")
    private String fileId;

    @TableField("batch")
    private Integer batch;

    @TableField("quality_plan_id")
    private Long qualityPlanId;

    @TableField("type")
    private Integer type;

    @TableField("specifications")
    private String specifications;

    @TableField("is_ban")
    private Integer isBan;

    @TableField("sku_value_md5")
    private String skuValueMd5;
    //
    @TableField("create_time")
    private Date createTime;
    //    @TableField("")
//    private Long customerId;
//    //仓库
//    @TableField("")
//    private String storeName;
//    //库位
//    @TableField("")
//    private String storePosName;
//    //材质名称
//    @TableField("")
//    private String materiaName;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSkuValue() {
        return skuValue;
    }

    public void setSkuValue(String skuValue) {
        this.skuValue = skuValue;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Integer getBomNum() {
        return bomNum;
    }

    public void setBomNum(Integer bomNum) {
        this.bomNum = bomNum;
    }

    public Integer getShipNum() {
        return shipNum;
    }

    public void setShipNum(Integer shipNum) {
        this.shipNum = shipNum;
    }

    public Integer getStockNum() {
        return stockNum;
    }

    public void setStockNum(Integer stockNum) {
        this.stockNum = stockNum;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getStockWarningMin() {
        return stockWarningMin;
    }

    public void setStockWarningMin(Integer stockWarningMin) {
        this.stockWarningMin = stockWarningMin;
    }

    public Integer getStockWarningMax() {
        return stockWarningMax;
    }

    public void setStockWarningMax(Integer stockWarningMax) {
        this.stockWarningMax = stockWarningMax;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    public String getModelCoding() {
        return modelCoding;
    }

    public void setModelCoding(String modelCoding) {
        this.modelCoding = modelCoding;
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

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public String getHeatTreatment() {
        return heatTreatment;
    }

    public void setHeatTreatment(String heatTreatment) {
        this.heatTreatment = heatTreatment;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSkuSize() {
        return skuSize;
    }

    public void setSkuSize(String skuSize) {
        this.skuSize = skuSize;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
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

    public Integer getMaintenancePeriod() {
        return maintenancePeriod;
    }

    public void setMaintenancePeriod(Integer maintenancePeriod) {
        this.maintenancePeriod = maintenancePeriod;
    }

    public Integer getAddMethod() {
        return addMethod;
    }

    public void setAddMethod(Integer addMethod) {
        this.addMethod = addMethod;
    }

    public String getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(String enclosure) {
        this.enclosure = enclosure;
    }

    public String getDrawing() {
        return drawing;
    }

    public void setDrawing(String drawing) {
        this.drawing = drawing;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Integer getBatch() {
        return batch;
    }

    public void setBatch(Integer batch) {
        this.batch = batch;
    }

    public Long getQualityPlanId() {
        return qualityPlanId;
    }

    public void setQualityPlanId(Long qualityPlanId) {
        this.qualityPlanId = qualityPlanId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public Integer getIsBan() {
        return isBan;
    }

    public void setIsBan(Integer isBan) {
        this.isBan = isBan;
    }

    public String getSkuValueMd5() {
        return skuValueMd5;
    }

    public void setSkuValueMd5(String skuValueMd5) {
        this.skuValueMd5 = skuValueMd5;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SkuList{" +
                "skuId=" + skuId +
                ", standard='" + standard + '\'' +
                ", skuName='" + skuName + '\'' +
                ", price=" + price +
                ", brandName='" + brandName + '\'' +
                ", customerName='" + customerName + '\'' +
                ", skuValue='" + skuValue + '\'' +
                ", spuId=" + spuId +
                ", spuName='" + spuName + '\'' +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", materialId=" + materialId +
                ", materialName='" + materialName + '\'' +
                ", bomNum=" + bomNum +
                ", shipNum=" + shipNum +
                ", stockNum=" + stockNum +
                ", unitId=" + unitId +
                ", unitName='" + unitName + '\'' +
                ", stockWarningMin=" + stockWarningMin +
                ", stockWarningMax=" + stockWarningMax +
                ", remarks='" + remarks + '\'' +
                ", coding='" + coding + '\'' +
                ", modelCoding='" + modelCoding + '\'' +
                ", viewFrame='" + viewFrame + '\'' +
                ", model='" + model + '\'' +
                ", packaging='" + packaging + '\'' +
                ", heatTreatment='" + heatTreatment + '\'' +
                ", level='" + level + '\'' +
                ", skuSize='" + skuSize + '\'' +
                ", color='" + color + '\'' +
                ", weight='" + weight + '\'' +
                ", partNo='" + partNo + '\'' +
                ", nationalStandard='" + nationalStandard + '\'' +
                ", maintenancePeriod=" + maintenancePeriod +
                ", addMethod=" + addMethod +
                ", enclosure='" + enclosure + '\'' +
                ", drawing='" + drawing + '\'' +
                ", images='" + images + '\'' +
                ", fileId='" + fileId + '\'' +
                ", batch=" + batch +
                ", qualityPlanId=" + qualityPlanId +
                ", type=" + type +
                ", specifications='" + specifications + '\'' +
                ", isBan=" + isBan +
                ", skuValueMd5='" + skuValueMd5 + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
