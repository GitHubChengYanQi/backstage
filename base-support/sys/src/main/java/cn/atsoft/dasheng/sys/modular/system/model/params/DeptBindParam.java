package cn.atsoft.dasheng.sys.modular.system.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
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
public class DeptBindParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


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

    @Override
    public String checkParam() {
        return null;
    }

}
