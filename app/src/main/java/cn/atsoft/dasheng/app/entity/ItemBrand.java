package cn.atsoft.dasheng.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 商品品牌绑定表
 * </p>
 *
 * @author 
 * @since 2021-09-23
 */
@TableName("daoxin_erp_item_brand")
public class ItemBrand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
      @TableId(value = "item_id", type = IdType.ID_WORKER)
    private Long itemId;

    /**
     * 品牌id
     */
    @TableField("brand_id")
    private Long brandId;

    /**
     * 品牌名称
     */
    @TableField("brand_name")
    private String brandName;


    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Override
    public String toString() {
        return "ItemBrand{" +
        "itemId=" + itemId +
        ", brandId=" + brandId +
        ", brandName=" + brandName +
        "}";
    }
}
