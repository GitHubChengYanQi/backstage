package cn.atsoft.dasheng.production.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 领取物料码
 * </p>
 *
 * @author cheng
 * @since 2022-03-29
 */
@Data
@ApiModel
public class ProductionPickCodeResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 取件码id
     */
    @ApiModelProperty("取件码id")
    private Long pickCodeId;

    /**
     * 取件码
     */
    @ApiModelProperty("取件码")
    private Long code;

    /**
     * 关联领料单id
     */
    @ApiModelProperty("关联领料单id")
    private Long pickListsId;

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
