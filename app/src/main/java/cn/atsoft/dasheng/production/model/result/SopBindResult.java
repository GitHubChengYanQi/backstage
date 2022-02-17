package cn.atsoft.dasheng.production.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * sop 绑定 工序
 * </p>
 *
 * @author song
 * @since 2022-02-17
 */
@Data
@ApiModel
public class SopBindResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 绑定id
     */
    @ApiModelProperty("绑定id")
    private Long bindId;

    @ApiModelProperty("")
    private Long sopId;

    @ApiModelProperty("")
    private Long shipSetpId;

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
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
