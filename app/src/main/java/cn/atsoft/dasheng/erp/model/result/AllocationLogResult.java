package cn.atsoft.dasheng.erp.model.result;

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
 * @since 2022-08-11
 */
@Data
@ApiModel
public class AllocationLogResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String coding;
    /**
     * 调拨logid
     */
    @ApiModelProperty("调拨logid")
    private Long allocationLogId;

    /**
     * 调拨id
     */
    @ApiModelProperty("调拨id")
    private Long allocationId;

    @ApiModelProperty("")
    private Long allocationDetailId;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
