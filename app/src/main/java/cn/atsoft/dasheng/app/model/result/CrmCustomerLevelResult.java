package cn.atsoft.dasheng.app.model.result;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 客户级别表
 * </p>
 *
 * @author
 * @since 2021-07-30
 */
@Data
@ApiModel
public class CrmCustomerLevelResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long rank;
    /**
     * 主键
     */

    /**
     * 客户级别id
     */
    @ApiModelProperty("客户级别id")
    private Long customerLevelId;

    private String remake;

    /**
     * 级别
     */
    @ApiModelProperty("级别")
    private String level;

    private Integer status;


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

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
    private Long deptId;
}
