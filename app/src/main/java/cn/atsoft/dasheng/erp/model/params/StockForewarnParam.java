package cn.atsoft.dasheng.erp.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 库存预警设置
 * </p>
 *
 * @author sjl
 * @since 2022-12-05
 */
@Data
@ApiModel
public class StockForewarnParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private List<StockForewarnParam> params;

    private List<Long> skuIds;
    private String update;

    private String content;
    //查询文字输入
    private String keyWords;
//    spuClassId
    private Long classId;
    private Integer storageMax;
    private Integer storageMin;

    // spuClassIds
    private List<Long> classIds;
    //预警状态
    private String forewarnStatus;
    private Long bomId;

    /**
     * 预警序号
     */
    @ApiModelProperty("预警序号")
    private Long forewarnId;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

    /**
     * 预警内容
     */
    @ApiModelProperty("预警内容")
    private Long formId;

    /**
     * 库存下限
     */
    @ApiModelProperty("库存下限")
    private Integer inventoryFloor;
    /**
     * 库存下限
     */
    @ApiModelProperty("库存下限")
    private Integer inventoryFloorMax;
    /**
     * 库存下限
     */
    @ApiModelProperty("库存下限")
    private Integer inventoryFloorMin;

    /**
     * 库存上限
     */
    @ApiModelProperty("库存上限")
    private Integer inventoryCeiling;


    /**
     * 库存上限
     */
    @ApiModelProperty("库存上限")
    private Integer inventoryCeilingMax;


    /**
     * 库存上限
     */
    @ApiModelProperty("库存上限")
    private Integer inventoryCeilingMin;



    /**
     * 库存数量
     */
    @ApiModelProperty("库存数量")
    private Integer numberMax;


    /**
     * 库存数量
     */
    @ApiModelProperty("库存数量")
    private Integer numberMin;



    /**
     * 库存上限
     */
    @ApiModelProperty("在途数量")
    private Integer floatingCargoNumberMax;


    /**
     * 库存上限
     */
    @ApiModelProperty("在途数量")
    private Integer floatingCargoNumberMin;

    /**
     * 待采数量
     */
    @ApiModelProperty("待采数量")
    private Integer purchaseNumberMax;

    /**
     * 待采数量
     */
    @ApiModelProperty("待采数量")
    private Integer purchaseNumberMin;

    /**
     * 删除状态
     */
    @ApiModelProperty("删除状态")
    private Integer display;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
