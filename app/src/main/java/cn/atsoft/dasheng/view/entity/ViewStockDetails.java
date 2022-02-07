package cn.atsoft.dasheng.view.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author 
 * @since 2022-01-27
 */
@TableName("view_stock_details")
public class ViewStockDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("sku_id")
    private Long skuId;

    /**
     * 物品Id
     */
    @TableField("spu_id")
    private Long spuId;

    /**
     * spu分类id
     */
    @TableField("spu_classification_id")
    private Long spuClassificationId;

    /**
     * 名称
     */
    @TableField("class_name")
    private String className;

    /**
     * 数量
     */
    @TableField("number")
    private Long number;

    /**
     * 仓库库位id
     */
    @TableField("storehouse_positions_id")
    private Long storehousePositionsId;

    /**
     * 物品名字
     */
    @TableField("spu_name")
    private String spuName;

    /**
     * 地点id
     */
    @TableField("storehouse_id")
    private Long storehouseId;

    /**
     * 仓库id
     */
    @TableField("stock_id")
    private Long stockId;

    /**
     * 品牌id
     */
    @TableField("brand_id")
    private Long brandId;


    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Long getSpuClassificationId() {
        return spuClassificationId;
    }

    public void setSpuClassificationId(Long spuClassificationId) {
        this.spuClassificationId = spuClassificationId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getStorehousePositionsId() {
        return storehousePositionsId;
    }

    public void setStorehousePositionsId(Long storehousePositionsId) {
        this.storehousePositionsId = storehousePositionsId;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public Long getStorehouseId() {
        return storehouseId;
    }

    public void setStorehouseId(Long storehouseId) {
        this.storehouseId = storehouseId;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    @Override
    public String toString() {
        return "ViewStockDetails{" +
        "skuId=" + skuId +
        ", spuId=" + spuId +
        ", spuClassificationId=" + spuClassificationId +
        ", className=" + className +
        ", number=" + number +
        ", storehousePositionsId=" + storehousePositionsId +
        ", spuName=" + spuName +
        ", storehouseId=" + storehouseId +
        ", stockId=" + stockId +
        ", brandId=" + brandId +
        "}";
    }
}