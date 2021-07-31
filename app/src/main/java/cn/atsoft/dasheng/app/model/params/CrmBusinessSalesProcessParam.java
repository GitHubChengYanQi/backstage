package cn.atsoft.dasheng.app.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * <p>
 * 销售流程
 * </p>
 *
 * @author 
 * @since 2021-07-31
 */
@Data
@ApiModel
public class CrmBusinessSalesProcessParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 销售流程
     */
    @ApiModelProperty("销售流程")
    private Long salesProcessId;

    /**
     * 流程名称
     */
    @ApiModelProperty("流程名称")
    private String name1;

    @ApiModelProperty("")
    private String name2;

    @ApiModelProperty("")
    private String name3;

    @ApiModelProperty("")
    private String name4;

    @ApiModelProperty("")
    private String name5;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer state;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
