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
 * 销售
 * </p>
 *
 * @author 
 * @since 2021-07-31
 */
@Data
@ApiModel
public class CrmBusinessSalesParam implements Serializable, BaseValidatingParam {

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

    @Override
    public String checkParam() {
        return null;
    }

}
