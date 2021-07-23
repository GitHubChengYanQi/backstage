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
 * 零件表
 * </p>
 *
 * @author 
 * @since 2021-07-23
 */
@Data
@ApiModel
public class PartsListParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 零件表id
     */
    @ApiModelProperty("零件表id")
    private Long partsListId;

    /**
     * 零件id
     */
    @ApiModelProperty("零件id")
    private Long items;

    /**
     * 零件名称
     */
    @ApiModelProperty("零件名称")
    private String name;

    /**
     * 零件数量
     */
    @ApiModelProperty("零件数量")
    private Integer number;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
