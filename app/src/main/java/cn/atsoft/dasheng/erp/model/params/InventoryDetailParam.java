package cn.atsoft.dasheng.erp.model.params;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 盘点任务详情
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-27
 */
@Data
@ApiModel
public class InventoryDetailParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private String type;

    private Long spuId;

    private List<Long> classIds;

    private List<Long> brandIds;

    private List<Long> positionIds;

    private List<Long> bomIds;

    private List<Long> inventoryIds;

    private Integer lockStatus;

    private Long positionId;

    private Long skuId;

    private List<Long> mediaIds;

    private Long realNumber;

    private Long anomalyId;

    private Long customerId;

    private String enclosure;

    private Long brandId;


    private Long storeHouseId;


    private Long qrcodeId;

    private Long number;
    /**
     * 盘点任务详情id
     */
    @ApiModelProperty("盘点任务详情id")
    private Long detailId;


    private Integer status;
    /**
     * 对应实物id
     */
    @ApiModelProperty("对应实物id")
    private Long inkindId;
    /**
     * 主表 Id
     */
    @ApiModelProperty("主表盘点任务id")
    private Long inventoryId;


    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
