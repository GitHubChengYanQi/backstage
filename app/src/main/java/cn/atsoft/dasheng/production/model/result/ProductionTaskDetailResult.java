package cn.atsoft.dasheng.production.model.result;

import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.production.model.request.JobBookingDetailCount;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-22
 */
@Data
@ApiModel
public class ProductionTaskDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private JobBookingDetailCount jobBookingDetailCount;
    private SkuResult outSkuResult;

    /**
     * 子表id
     */
    @ApiModelProperty("子表id")
    private Long productionTaskDetailId;

    /**
     * 主表id
     */
    @ApiModelProperty("主表id")
    private Long productionTaskId;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;


    private Long outSkuNumber;
    private Integer number;

    /**
     * 实物
     */
    @ApiModelProperty("实物")
    private Long inkindId;

    private Long outSkuId;

    private Long skuId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
