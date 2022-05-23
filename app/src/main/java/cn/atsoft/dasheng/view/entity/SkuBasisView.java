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
 * @since 2022-05-20
 */
@TableName("sku_basis_view")
public class SkuBasisView implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("sku_id")
    private Long skuId;

    /**
     * sku名字
     */
    @TableField("sku_name")
    private String skuName;

    /**
     * 物品Id
     */
    @TableField("spu_id")
    private Long spuId;

    /**
     * 物品名字
     */
    @TableField("spu_name")
    private String spuName;

    /**
     * 名称
     */
    @TableField("spu_class_name")
    private String spuClassName;

    /**
     * spu分类id
     */
    @TableField("spu_classification_id")
    private Long spuClassificationId;


    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
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

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public String getSpuClassName() {
        return spuClassName;
    }

    public void setSpuClassName(String spuClassName) {
        this.spuClassName = spuClassName;
    }

    public Long getSpuClassificationId() {
        return spuClassificationId;
    }

    public void setSpuClassificationId(Long spuClassificationId) {
        this.spuClassificationId = spuClassificationId;
    }

    @Override
    public String toString() {
        return "SkuBasisView{" +
        "skuId=" + skuId +
        ", skuName=" + skuName +
        ", spuId=" + spuId +
        ", spuName=" + spuName +
        ", spuClassName=" + spuClassName +
        ", spuClassificationId=" + spuClassificationId +
        "}";
    }
}
