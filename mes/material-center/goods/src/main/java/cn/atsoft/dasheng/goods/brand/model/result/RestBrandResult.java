package cn.atsoft.dasheng.goods.brand.model.result;


import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 品牌表
 * </p>
 *
 * @author 1
 * @since 2021-07-14
 */
@Data
@ApiModel
public class RestBrandResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long anomalyId;
    //     private PartsResult partsResult;
    private List<Long> skuIds;

//    private List<SkuResult> skuResults;

//    private List<StorehousePositionsResult> positionsResults;
    private List<RestBrandResult> restBrandResults;

    private Long positionId;

    private Long number = 0L;

    private Integer inventoryStatus;

    private Long inkind;
    /**
     * 品牌id
     */
    @ApiModelProperty("品牌id")
    private Long brandId;


    private Integer num;

    /**
     * 品牌名称
     */
    @ApiModelProperty("品牌名称")
    private String brandName;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Date updateTime;

    /**
     * 创建用户
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Long createUser;

    /**
     * 修改用户
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Long updateUser;
    @JSONField(serialize = false)
    private Long deptId;
    /**
     * 状态
     */
    @ApiModelProperty("状态")
    @JSONField(serialize = false)
    private Integer display;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
