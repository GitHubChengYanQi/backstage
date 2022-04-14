package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 入库记录表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-04-14
 */
@Data
@ApiModel
public class InstockLogResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private Object createUserResult;
    private List<InstockLogDetailResult> detailResults;

    private String remark;

    private Date instockTime;

    @ApiModelProperty("")
    private Long instockLogId;

    @ApiModelProperty("")
    private Long instockOrderId;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty("")
    private Integer display;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
