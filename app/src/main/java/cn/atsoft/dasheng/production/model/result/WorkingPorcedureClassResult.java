package cn.atsoft.dasheng.production.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 工序分类表
 * </p>
 *
 * @author 
 * @since 2021-10-29
 */
@Data
@ApiModel
public class WorkingPorcedureClassResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 工序分类id
     */
    @ApiModelProperty("工序分类id")
    private Long wpClassId;

    /**
     * 工序分类名称
     */
    @ApiModelProperty("工序分类名称")
    private String wpClassName;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
