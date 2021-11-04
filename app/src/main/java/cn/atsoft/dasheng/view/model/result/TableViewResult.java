package cn.atsoft.dasheng.view.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2021-11-04
 */
@Data
@ApiModel
public class TableViewResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 表视图主键
     */
    @ApiModelProperty("表视图主键")
    private Long tableViewId;

    @ApiModelProperty("")
    private String tableKey;

    @ApiModelProperty("")
    private Long userId;

    @ApiModelProperty("")
    private String field;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
