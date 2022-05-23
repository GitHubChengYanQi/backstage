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
@TableName("sku_position_view")
public class SkuPositionView implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * sku名字
     */
    @TableField("sku_name")
    private String skuName;

    @TableField("sku_id")
    private Long skuId;

    /**
     * 仓库库位id
     */
    @TableField("storehouse_positions_id")
    private Long storehousePositionsId;

    /**
     * 库位名称
     */
    @TableField("name")
    private String name;


    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getStorehousePositionsId() {
        return storehousePositionsId;
    }

    public void setStorehousePositionsId(Long storehousePositionsId) {
        this.storehousePositionsId = storehousePositionsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SkuPositionView{" +
        "skuName=" + skuName +
        ", skuId=" + skuId +
        ", storehousePositionsId=" + storehousePositionsId +
        ", name=" + name +
        "}";
    }
}
