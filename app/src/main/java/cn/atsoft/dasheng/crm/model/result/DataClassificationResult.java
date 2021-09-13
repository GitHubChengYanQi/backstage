package cn.atsoft.dasheng.crm.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 资料分类表
 * </p>
 *
 * @author 
 * @since 2021-09-13
 */
@Data
@ApiModel
public class DataClassificationResult implements Serializable {

    private static final long serialVersionUID = 1L;
private  List<DataResult> dataResults;

    /**
     * 资料分类id
     */
    @ApiModelProperty("资料分类id")
    private Long dataClassificationId;

    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    private String title;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Long sort;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
