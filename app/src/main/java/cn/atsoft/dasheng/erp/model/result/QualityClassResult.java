package cn.atsoft.dasheng.erp.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 质检方案详情分类
 * </p>
 *
 * @author song
 * @since 2021-10-28
 */
@Data
@ApiModel
public class QualityClassResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 质检方案分类id
     */
    @ApiModelProperty("质检方案分类id")
    private Long qualityPlanClassId;

    /**
     * 质检方案id
     */
    @ApiModelProperty("质检方案id")
    private Long qualityPlanId;

    /**
     * 质检项分类id
     */
    @ApiModelProperty("质检项分类id")
    private Long qualityCheckClassificationId;

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
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    private String name;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
