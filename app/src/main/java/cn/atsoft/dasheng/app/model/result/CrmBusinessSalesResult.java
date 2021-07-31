package cn.atsoft.dasheng.app.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 销售
 * </p>
 *
 * @author 
 * @since 2021-07-31
 */
@Data
@ApiModel
public class CrmBusinessSalesResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 销售id
     */
    @ApiModelProperty("销售id")
    private Long salesId;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
