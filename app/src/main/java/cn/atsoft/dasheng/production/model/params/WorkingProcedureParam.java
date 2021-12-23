package cn.atsoft.dasheng.production.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 工序表
 * </p>
 *
 * @author
 * @since 2021-10-29
 */
@Data
@ApiModel
public class WorkingProcedureParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 工序
     */
    @ApiModelProperty("工序")
    private Long workingProcedureId;

    /**
     * 人员id
     */
    @ApiModelProperty("人员id")
    private Long userId;

    /**
     * 工位id
     */
    @ApiModelProperty("工位id")
    private Long productionStationId;

    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private String accessories;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 工序名称
     */
    @ApiModelProperty("工序名称	")
    private String workingProcedureName;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
