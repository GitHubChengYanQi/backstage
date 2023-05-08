package cn.atsoft.dasheng.sys.modular.system.model.result;

import cn.atsoft.dasheng.sys.modular.system.entity.Dept;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 各租户内 部门绑定表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-04
 */
@Data
@ApiModel
public class DeptBindResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Dept dept;

    @ApiModelProperty("")
    private Long deptBindId;

    @ApiModelProperty("")
    private Long deptId;

    @ApiModelProperty("")
    private Long userId;

    @ApiModelProperty("")
    private Long tenantId;
    @ApiModelProperty("")
    private Integer mainDept;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
