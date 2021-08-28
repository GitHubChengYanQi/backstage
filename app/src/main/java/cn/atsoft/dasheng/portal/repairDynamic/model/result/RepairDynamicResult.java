package cn.atsoft.dasheng.portal.repairDynamic.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 售后动态表
 * </p>
 *
 * @author 
 * @since 2021-08-24
 */
@Data
@ApiModel
public class RepairDynamicResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 售后动态id
     */
    @ApiModelProperty("售后动态id")
    private Long dynamicId;

    /**
     * 出厂编号id
     */
    @ApiModelProperty("出厂编号id")
    private Long repairId;

    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;

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
