package cn.atsoft.dasheng.erp.model.result;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
public class InventoryDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long skuId;

    private String type;

    private Object object;
    /**
     * 盘点任务详情id
     */
    @JSONField(serialize = false)
    @ApiModelProperty("盘点任务详情id")
    private Long detailId;

    /**
     * 主表 Id
     */
    @JSONField(serialize = false)
    @ApiModelProperty("主表盘点任务id")
    private Long inventoryId;

    @JSONField(serialize = false)
    private Integer status;
    /**
     * 对应实物id
     */
    @JSONField(serialize = false)
    @ApiModelProperty("对应实物id")
    private Long inkindId;

    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long createUser;

    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date createTime;

    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 部门id
     */
    @JSONField(serialize = false)
    @ApiModelProperty("部门id")
    private Long deptId;

    @JSONField(serialize = false)
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
