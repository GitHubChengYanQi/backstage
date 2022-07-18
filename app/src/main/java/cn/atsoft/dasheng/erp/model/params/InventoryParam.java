package cn.atsoft.dasheng.erp.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 盘点任务主表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-27
 */
@Data
@ApiModel
public class InventoryParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private Long status;

    private String coding;

    private Boolean allSku = false;

    private Boolean AllBom = false;

    private String enclosure;

    private String participants;

    private List<InventoryDetailParam> detailParams;

    private List<InventoryStockParam> stockParams;

    private InventoryDetailParam detailParam;

    private String notice;


    private Long brandId;

    private Long positionId;

    private Date currentTime;

    private Date endTime;


    private Date beginTime;


    private String mode;


    private String method;
    /**
     * 盘点任务id
     */
    @ApiModelProperty("盘点任务id")
    private Long inventoryTaskId;

    /**
     * 盘点任务名称
     */
    @ApiModelProperty("盘点任务名称")
    private String inventoryTaskName;

    /**
     * 盘点任务备注
     */
    @ApiModelProperty("盘点任务备注")
    private String remark;

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
