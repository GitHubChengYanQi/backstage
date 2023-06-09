package cn.atsoft.dasheng.view.model.result;

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
 * @author 
 * @since 2021-11-04
 */
@Data
@ApiModel
public class TableViewResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 表视图主键
     */
    @ApiModelProperty("表视图主键")
    private Long tableViewId;

    @ApiModelProperty("")
    private String tableKey;

    @ApiModelProperty("")
    private Long userId;

    @ApiModelProperty("")
    private String field;

    private String name;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

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
