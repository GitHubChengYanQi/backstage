package cn.atsoft.dasheng.production.model.result;

import lombok.Data;
import java.util.Date;
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
public class WorkingProcedureResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;
    private String productionStationName;
    /**
     * 工序
     */
    @ApiModelProperty("工序")
    private Long 
workingProcedureId;

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
     * 分类
     */
    @ApiModelProperty("工序分类")
    private String workingProcedureClass;
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
}
