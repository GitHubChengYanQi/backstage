package cn.atsoft.dasheng.app.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 物品表
 * </p>
 *
 * @author 1
 * @since 2021-07-14
 */
@Data
@ApiModel
public class ItemsResult implements Serializable {

    private static final long serialVersionUID = 1L;

     private  MaterialResult materialResult;
    /**
     * 物品Id
     */
    @ApiModelProperty("物品Id")
    private Long itemId;

    /**
     * 物品名字
     */
    @ApiModelProperty("物品名字")
    private String name;

    /**
     * 质保期
     */
    @ApiModelProperty("质保期")
    private Integer shelfLife;

    /**
     * 物品库存
     */
    @ApiModelProperty("物品库存")
    private Integer inventory;

    /**
     * 生产日期
     */
    @ApiModelProperty("生产日期")
    private Date productionTime;

    /**
     * 重要程度
     */
    @ApiModelProperty("重要程度")
    private Float important;

    /**
     * 物品重量
     */
    @ApiModelProperty("物品重量")
    private Double weight;

    /**
     * 材质id
     */
    @ApiModelProperty("材质id")
    private Long materialId;

    /***
     *材质姓名
     */

    private String materialName;


    /**
     * 成本
     */
    @ApiModelProperty("成本")
    private Integer cost;

    /**
     * 易损
     */
    @ApiModelProperty("易损")
    private Integer vulnerability;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
