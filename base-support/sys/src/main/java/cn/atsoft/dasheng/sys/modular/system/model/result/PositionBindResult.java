package cn.atsoft.dasheng.sys.modular.system.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 租户用户位置绑定表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-09
 */
@Data
@ApiModel
public class PositionBindResult implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private Long positionBindId;

    @ApiModelProperty("")
    private Long positionId;

    @ApiModelProperty("")
    private Long userId;

    @ApiModelProperty("")
    private Long tenantId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
